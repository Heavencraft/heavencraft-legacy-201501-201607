package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.database.users.UpdateJobExperienceQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.worlds.WorldsManager;

public class JobActionListener extends AbstractListener<HeavenPlugin>
{
	private final Map<UUID, Integer> pointsByPlayer = new HashMap<UUID, Integer>();

	public JobActionListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	private void dispatchJobAction(Player player, JobAction action) throws HeavenException
	{
		dispatchJobAction(player, action, 1);
	}

	private void dispatchJobAction(Player player, JobAction action, int noActions) throws HeavenException
	{
		final User user = UserProvider.getUserByName(player.getName());

		final Job job = user.getJob();
		if (job == null)
			return; // Nothing to do

		int pointsToAdd = job.getPointsForAction(action);
		if (pointsToAdd == 0)
			return; // Nothing to do

		final Rank rank = Rank.getRankByLevel(JobUtil.getLevelFromXp(user.getJobExperience()));
		pointsToAdd = (int) (pointsToAdd * noActions * rank.getPointsMultiplier());

		Integer currentPoints = pointsByPlayer.remove(player.getUniqueId());

		if (currentPoints == null)
			currentPoints = pointsToAdd;
		else
			currentPoints += pointsToAdd;

		if (currentPoints >= 1000)
		{
			int xp = 0;

			do
			{
				xp++;
				currentPoints -= 1000;
			}
			while (currentPoints >= 1000);

			QueriesHandler.addQuery(new UpdateJobExperienceQuery(user, xp));
			QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, xp));
		}

		pointsByPlayer.put(player.getUniqueId(), currentPoints);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();

		// BREAK action doesn't apply in world
		if (player.getWorld() == WorldsManager.getWorld())
			return;

		// Effect of Bone Meal should not be counted
		if (isBoneMeal(event.getItemInHand()))
			return;

		dispatchJobAction(player, new JobAction(JobActionType.BREAK, new BlockType(event.getBlock())), -1);
	}

	private static boolean isBoneMeal(ItemStack item)
	{
		return item != null //
				&& item.getType() == Material.INK_SACK //
				&& ((Dye) item.getData()).getColor() == DyeColor.WHITE;
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();

		// BREAK action doesn't apply in world
		if (player.getWorld() == WorldsManager.getWorld())
			return;

		dispatchJobAction(player, new JobAction(JobActionType.BREAK, new BlockType(event.getBlock())));
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerFish(PlayerFishEvent event) throws HeavenException
	{
		if (event.getState() != State.CAUGHT_FISH)
			return;

		final Entity caught = event.getCaught();

		if (caught.getType() != EntityType.DROPPED_ITEM)
			return;

		dispatchJobAction(event.getPlayer(),
				new JobAction(JobActionType.FISH, new BlockType(((Item) caught).getItemStack())));
	}

	@EventHandler
	private void onEntityDeath(EntityDeathEvent event) throws HeavenException
	{
		final Player player = event.getEntity().getKiller();

		if (player == null)
			return;

		dispatchJobAction(player, new JobAction(JobActionType.KILL, event.getEntityType()));
	}

	@EventHandler(ignoreCancelled = true)
	private void onCraftItem(CraftItemEvent event) throws HeavenException
	{
		final ItemStack currentItem = event.getCurrentItem();
		if (currentItem == null)
			return;

		final HumanEntity whoClicked = event.getWhoClicked();
		if (whoClicked.getType() != EntityType.PLAYER)
			return;

		final Player player = (Player) whoClicked;
		final JobAction craftAction = new JobAction(JobActionType.CRAFT, new BlockType(currentItem));

		if (event.isShiftClick())
		{
			int minAmout = Integer.MAX_VALUE;

			for (final ItemStack item : event.getInventory().getMatrix())
				if (item != null && item.getType() != Material.AIR && item.getAmount() < minAmout)
					minAmout = item.getAmount();

			dispatchJobAction(player, craftAction, minAmout);
		}
		else
		{
			dispatchJobAction(player, craftAction);
		}
	}

	@EventHandler()
	private void onFurnaceExtract(FurnaceExtractEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();

		dispatchJobAction(player, new JobAction(JobActionType.BAKE, new BlockType(event.getBlock())),
				event.getItemAmount());
	}
}
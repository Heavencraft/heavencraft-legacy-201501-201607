package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class JobActionListener extends AbstractListener<HeavenPlugin>
{
	private final Map<UUID, Integer> pointsByPlayer = new HashMap<UUID, Integer>();

	protected JobActionListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	private void dispatchJobAction(Player player, JobAction action) throws HeavenException
	{
		User user = UserProvider.getUserByName(player.getName());

		Job job = user.getJob();
		if (job == null)
			return; // Nothing to do

		int pointsToAdd = job.getPointsForAction(action);
		if (pointsToAdd == 0)
			return; // Nothing to do

		Rank rank = Rank.getRankByLevel(JobUtil.getLevelFromXp(user.getJobExperience()));
		pointsToAdd = (int) (pointsToAdd * rank.getPointsMultiplier());

		Integer currentPoints = pointsByPlayer.remove(player.getUniqueId());

		if (currentPoints == null)
			currentPoints = pointsToAdd;
		else
			currentPoints += pointsToAdd;

		if (currentPoints > 1000)
		{
			currentPoints -= 1000;
			QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, 1));
		}

		pointsByPlayer.put(player.getUniqueId(), currentPoints);
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event) throws HeavenException
	{
		dispatchJobAction(event.getPlayer(), new JobAction(JobActionType.BREAK, event.getBlock().getType()));
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerFish(PlayerFishEvent event) throws HeavenException
	{
		if (event.getState() != State.CAUGHT_FISH)
			return;

		dispatchJobAction(event.getPlayer(), new JobAction(JobActionType.FISH, event.getCaught().getType()));
	}
}
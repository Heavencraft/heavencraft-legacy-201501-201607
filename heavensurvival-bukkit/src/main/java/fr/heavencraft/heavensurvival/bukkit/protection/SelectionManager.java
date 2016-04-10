package fr.heavencraft.heavensurvival.bukkit.protection;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class SelectionManager extends AbstractListener<HeavenPlugin>
{
	private static final String FIRST_POINT = "Premier point placé en x = {%1$s}, z = {%2$s}.";
	private static final String SECOND_POINT = "Second point placé en x = {%1$s}, z = {%2$s}.";
	private static final String SELECTION_PRICE = "La protection vous coûtera {%1$s} pépites d'or.";

	private static final Map<UUID, Selection> selections = new HashMap<UUID, Selection>();

	public SelectionManager(HeavenPlugin plugin)
	{
		super(plugin);
	}

	public static void enable(Player player)
	{
		final UUID uniqueId = player.getUniqueId();

		if (!selections.containsKey(uniqueId))
			selections.put(uniqueId, new Selection());
	}

	public static void disable(Player player)
	{
		selections.remove(player.getUniqueId());
	}

	public static Selection getSelection(Player player) throws HeavenException
	{
		final Selection result = selections.get(player.getUniqueId());

		if (result == null || !result.isValid())
			throw new HeavenException("Vous devez sélectionner votre zone avec un bâton.");

		return result;
	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();

		if (event.getItem() == null || event.getItem().getType() != Material.STICK)
			return;

		if (event.getClickedBlock() == null)
			return;

		final Location location = event.getClickedBlock().getLocation();
		event.setCancelled(true);

		final Selection selection = selections.get(player.getUniqueId());
		if (selection == null)
			return;

		switch (event.getAction())
		{
			case LEFT_CLICK_BLOCK:
				selection.setLeft(location);
				ChatUtil.sendMessage(player, FIRST_POINT, location.getBlockX(), location.getBlockZ());
				break;

			case RIGHT_CLICK_BLOCK:
				selection.setRight(location);
				ChatUtil.sendMessage(player, SECOND_POINT, location.getBlockX(), location.getBlockZ());
				break;

			default:
				break;
		}

		if (selection.isValid())
			ChatUtil.sendMessage(player, SELECTION_PRICE, selection.getPrice());
	}
}
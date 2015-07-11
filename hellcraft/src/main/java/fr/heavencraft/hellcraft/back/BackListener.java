package fr.heavencraft.hellcraft.back;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class BackListener extends AbstractListener<HeavenPlugin>
{
	private static final Map<String, Location> deathLocation = new HashMap<String, Location>();

	public BackListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	private void onPlayerDeath(PlayerDeathEvent event)
	{
		final Player player = event.getEntity();
		deathLocation.put(player.getName(), player.getLocation());
	}

	public static Location getDeathLocation(String playerName)
	{
		return deathLocation.get(playerName);
	}
}
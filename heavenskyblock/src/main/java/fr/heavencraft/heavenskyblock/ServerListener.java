package fr.heavencraft.heavenskyblock;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class ServerListener extends AbstractListener<HeavenSkyblock>
{
	private final Location spawnLocation;

	protected ServerListener(HeavenSkyblock plugin, Location spawnLocation)
	{
		super(plugin);
		this.spawnLocation = spawnLocation;
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();

		if (!player.hasPlayedBefore())
			player.teleport(spawnLocation);

		player.sendMessage(ChatColor.GREEN + " * Bienvenue sur le SkyBlock o/");
	}
}
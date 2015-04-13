package fr.heavencraft.heavencore.bukkit.listeners;

import java.util.Arrays;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

public class WorldAccessListener extends AbstractListener
{
	private final Location fallbackLocation;
	private final Collection<String> worlds;

	public WorldAccessListener(HeavenPlugin plugin, Location fallbackLocation, String... worlds)
	{
		super(plugin);

		this.fallbackLocation = fallbackLocation;
		this.worlds = Arrays.asList(worlds);
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerMove(PlayerMoveEvent event)
	{
		final String worldName = event.getTo().getWorld().getName();

		// This world don't have limited access
		if (!worlds.contains(worldName))
			return;

		final Player player = event.getPlayer();

		// The player have the access : everything is OK
		if (player.hasPermission(CorePermissions.WORLD_ACCESS + worldName))
			return;

		event.setTo(fallbackLocation);
		plugin.sendMessage(player, "Vous n'avez pas la permission d'accéder au monde {%1$s}.", worldName);
	}
}
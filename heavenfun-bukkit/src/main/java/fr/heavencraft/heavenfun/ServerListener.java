package fr.heavencraft.heavenfun;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class ServerListener extends AbstractListener<BukkitHeavenFun>
{
	public ServerListener(BukkitHeavenFun plugin)
	{
		super(plugin);
	}

	@EventHandler
	private void onPlayerRespawn(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(BukkitHeavenFun.getSpawn());
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event)
	{
		event.getPlayer().teleport(BukkitHeavenFun.getSpawn());
	}
}
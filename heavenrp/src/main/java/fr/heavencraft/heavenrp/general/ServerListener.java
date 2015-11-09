package fr.heavencraft.heavenrp.general;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.worlds.WorldsManager;

public class ServerListener implements Listener
{
	public ServerListener()
	{
		Bukkit.getPluginManager().registerEvents(this, HeavenRP.getInstance());
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();

		if (!player.hasPlayedBefore())
			player.teleport(WorldsManager.getTutoLocation());

		player.sendMessage(ChatColor.GREEN + " * Bienvenue sur Heavencraft semi-RP !");
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		if (event.getChangedType() == Material.PORTAL)
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		event.setRespawnLocation(WorldsManager.getSpawn());
	}

	@EventHandler(ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER)
			return;

		if (event.getCause() != DamageCause.VOID)
			return;

		if (event.getEntity().getLocation().getY() > 0
				|| !event.getEntity().getLocation().getWorld().getName().equals("world"))
			return;

		event.setCancelled(true);
		event.getEntity().teleport(WorldsManager.getSpawn());
	}
}
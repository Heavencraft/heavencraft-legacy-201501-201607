package fr.heavencraft.heavencrea.worlds;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalEnterEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class PortalListener extends AbstractListener
{
	public PortalListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onEntityPortalEnter(EntityPortalEnterEvent event)
	{
		if (event.getEntityType() != EntityType.PLAYER)
			return;

		final Block block = event.getLocation().getBlock();

		if (block.getType() != Material.ENDER_PORTAL)
			return;

		if (block.getWorld() != WorldsManager.getWorldCreative())
			return;

		plugin.teleportPlayer((Player) event.getEntity(), WorldsManager.getWorldBiome().getSpawnLocation());
	}
}
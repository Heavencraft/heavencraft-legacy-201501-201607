package fr.heavencraft.heavenrp.worlds;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.heavencraft.Permissions;
import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.actions.TeleportPlayerAction;
import fr.heavencraft.utils.ChatUtil;
import fr.heavencraft.utils.DevUtil;

public class WorldsListener implements Listener
{
	public WorldsListener()
	{
		DevUtil.registerListener(this);
	}

	// Limites des mondes
	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		final Location l = event.getTo();

		// Monde semi-RP
		if (l.getWorld().equals(WorldsManager.getWorld()))
		{
			if (Math.pow(l.getX(), 2) + Math.pow(l.getZ(), 2) > 25000000)
			{
				event.setTo(WorldsManager.getSpawn());
				ChatUtil.sendMessage(event.getPlayer(), "Vous avez atteint la limite du monde semi-RP.");
				// event.setCancelled(true);
			}
		}

		// Monde ressources
		else if (l.getWorld().equals(WorldsManager.getResources()))
		{
			final int limit = WorldsManager.RESOURCES_SIZE / 2;

			if (Math.abs(l.getX()) > limit || Math.abs(l.getZ()) > limit)
			{
				event.setTo(WorldsManager.getSpawn());
				ChatUtil.sendMessage(event.getPlayer(), "Vous avez atteint la limite du monde ressources.");
				// event.setCancelled(true);
			}
		}

		else if (l.getWorld().getName().equals("world_old") || l.getWorld().getName().equals("world_origine")
				|| l.getWorld().getName().equals("world_dungeon") || l.getWorld().getName().equals("world_event"))
		{
			if (!event.getPlayer().hasPermission(Permissions.TPWORLD))
				event.getPlayer().teleport(WorldsManager.getSpawn());
		}
	}

	// Passage dans un portail
	@EventHandler
	public void onEntityPortalEnter(PlayerMoveEvent event)
	{
		final Location from = event.getFrom();
		final Location to = event.getTo();

		if (to.getWorld() != WorldsManager.getWorld())
			return;

		// Player enter into a portal
		final Block portalBlock = to.getBlock();
		if (from.getBlock().getType() != Material.PORTAL && portalBlock.getType() == Material.PORTAL)
		{
			final Location destination;
			switch (portalBlock.getRelative(BlockFace.DOWN).getType())
			{
				case NETHERRACK:
					destination = WorldsManager.getSpawnNether();
					break;
				case ENDER_STONE:
					destination = WorldsManager.getSpawnTheEnd();
					break;
				case SAND:
					destination = WorldsManager.getResourcesSpawn();
					break;
				default:
					return;
			}
			ActionsHandler.addAction(new TeleportPlayerAction(event.getPlayer(), destination));
		}
	}
}
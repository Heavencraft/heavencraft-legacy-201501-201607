package fr.heavencraft.heavenrp.worlds;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class WorldsListener extends AbstractListener<HeavenPlugin>
{
	public WorldsListener(HeavenPlugin plugin)
	{
		super(plugin);
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
			final int limit_with_warn_offset = limit - 20;

			if (Math.abs(l.getX()) > limit || Math.abs(l.getZ()) > limit)
			{
				event.setTo(WorldsManager.getSpawn());
				ChatUtil.sendMessage(event.getPlayer(), "Vous avez atteint la limite du monde ressources.");
				// event.setCancelled(true);
			}
			else if(Math.abs(l.getX()) > (limit_with_warn_offset) || Math.abs(l.getZ()) > (limit_with_warn_offset))
			{
				ChatUtil.sendMessage(event.getPlayer(), "Vous vous approchez de la limite du monde ressources.");
			}
		}

		else if (l.getWorld().getName().equals("world_old") || l.getWorld().getName().equals("world_origine")
				|| l.getWorld().getName().equals("world_dungeon") || l.getWorld().getName().equals("world_event")
				|| l.getWorld().getName().equals("world_noel"))
		{
			if (!event.getPlayer().hasPermission(CorePermissions.TPWORLD_COMMAND))
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
			PlayerUtil.teleportPlayer(event.getPlayer(), destination);
		}
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event)
	{
		final Chunk spawnChunk = WorldsManager.getSpawn().getChunk();
		// If is spawn chunk, do not unload
		if(event.getChunk().getWorld().getName().equals(spawnChunk.getWorld().getName()) 
				&& event.getChunk().getX() == spawnChunk.getX()
				&& event.getChunk().getZ() == spawnChunk.getZ())
		{
			event.setCancelled(true);
			return;
		}
	}
}
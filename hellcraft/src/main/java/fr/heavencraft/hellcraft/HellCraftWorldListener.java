package fr.heavencraft.hellcraft;

import java.util.Iterator;
import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class HellCraftWorldListener extends AbstractListener<HeavenPlugin>
{
	private static final int LIMIT_ENTITIES = 1500;

	public HellCraftWorldListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onChunkUnload(ChunkUnloadEvent event)
	{
		for (final Entity entity : event.getChunk().getEntities())
			if (entity instanceof Monster || entity instanceof Projectile)
				entity.remove();
	}

	@EventHandler(ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event)
	{
		final World world = event.getLocation().getWorld();

		if (!WorldsManager.getWorldCity().equals(world))
			return;

		final List<Entity> entities = world.getEntities();
		int nbEntitiesToRemove = entities.size() - LIMIT_ENTITIES;

		if (nbEntitiesToRemove > 0)
		{
			final Iterator<Entity> it = entities.iterator();

			while (it.hasNext() && nbEntitiesToRemove > 0)
			{
				final Entity entity = it.next();

				if (entity.getType() != EntityType.PLAYER && entity.getTicksLived() > 1200)
				{
					entity.remove();
					nbEntitiesToRemove--;
				}
			}
		}
	}
}
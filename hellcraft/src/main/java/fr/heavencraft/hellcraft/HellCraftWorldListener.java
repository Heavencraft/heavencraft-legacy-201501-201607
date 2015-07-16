package fr.heavencraft.hellcraft;

import java.util.List;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class HellCraftWorldListener extends AbstractListener<HeavenPlugin>
{
	private static final int LIMIT_ENTITIES = 1050;
	private static final int LIMIT_MONSTERS = 900;

	public HellCraftWorldListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onChunkLoad(ChunkLoadEvent event)
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

		if (entities.size() > LIMIT_ENTITIES)
		{
			int nbMonsters = 0;

			for (int i = entities.size(); i != 0; i--)
			{
				final Entity entity = entities.get(i - 1);

				if (entity instanceof Monster)
				{
					if (nbMonsters != LIMIT_MONSTERS)
						nbMonsters++;
					else
						entity.remove();
				}
			}

			if (nbMonsters != LIMIT_MONSTERS)
			{
				System.out.println("Nb monsters : " + nbMonsters);
			}
		}
	}
}
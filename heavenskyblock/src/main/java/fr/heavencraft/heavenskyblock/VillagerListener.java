package fr.heavencraft.heavenskyblock;

import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class VillagerListener extends AbstractListener<HeavenSkyblock>
{
	private static final String[] VILLAGER_NAMES =
	{ "Jean", "Michel", "Daniel", "Gérard", "Bernard", "Alain", "Jacques", "André", "Claude", "Pierre",
			"Christian", "Guy", "René", "Robert", "Roger", "Jean-Claude", "Jean-Pierre", "Georges", "Yves",
			"Marie", "Monique", "Nicole", "Françoise", "Danielle", "Jacqueline", "Christiane", "Annie",
			"Michele", "Josette", "Chantal", "Michelle", "Anne", "Colette", "Annick", "Jeannine", "Danièle",
			"Liliane", "Claudine" };

	private final Random rnd = new Random();

	protected VillagerListener(HeavenSkyblock plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (event.getEntityType() != EntityType.VILLAGER)
			return;

		event.getEntity().setCustomName(VILLAGER_NAMES[rnd.nextInt(VILLAGER_NAMES.length)]);
	}

	@EventHandler
	private void onChunkLoad(ChunkLoadEvent event)
	{
		for (final Entity entity : event.getChunk().getEntities())
		{
			if (entity.getType() == EntityType.VILLAGER
					&& (entity.getCustomName() == null || entity.getCustomName().isEmpty()))
			{
				entity.setCustomName(VILLAGER_NAMES[rnd.nextInt(VILLAGER_NAMES.length)]);
			}
		}
	}
}
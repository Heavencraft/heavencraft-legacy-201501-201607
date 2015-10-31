package fr.heavencraft.heavencore.bukkit.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

public class AntiCheatListener extends AbstractListener<HeavenPlugin>
{
	public AntiCheatListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	// OPTI : doit s'exécuter avant l'anti-lag.
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	private void onCreatureSpawn(CreatureSpawnEvent event)
	{
		switch (event.getEntityType())
		{
			case IRON_GOLEM:
				if (event.getSpawnReason() == SpawnReason.VILLAGE_DEFENSE)
					event.setCancelled(true);
				break;

			case VILLAGER:
				if (event.getSpawnReason() == SpawnReason.BREEDING)
					event.setCancelled(true);
				break;

			default:
				break;
		}
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event)
	{
		if (event.getBlock().getType() == Material.MOB_SPAWNER)
			event.setCancelled(true);
	}
}
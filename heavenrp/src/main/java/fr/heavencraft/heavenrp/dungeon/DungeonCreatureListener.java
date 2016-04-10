package fr.heavencraft.heavenrp.dungeon;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemDespawnEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.dungeon.DungeonManager.Dungeon;
import fr.heavencraft.heavenrp.dungeon.DungeonManager.DungeonRoom;

public class DungeonCreatureListener extends AbstractListener<HeavenPlugin>
{
	public DungeonCreatureListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	/**
	 * Track mobs spawning into a dungeons.
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		// It must be a mob
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		// Excluded mobs
		LivingEntity mob = (LivingEntity) event.getEntity();
		if (mob.getType() == EntityType.BAT || mob.getType() == EntityType.ENDERMAN)
			return;
		// Add to dungeon room if inside room
		DungeonRoom dgr = DungeonManager.getRoomByLocation(mob.getLocation());
		if (dgr == null)
			return;

		Dungeon dg = DungeonManager.getDungeon(dgr.getDungeonId());
		// Prevent spawn when dungeon is not running
		if (dg.DungeonState != DungeonManager.DungeonStates.RUNNING)
		{
			mob.remove();
			event.setCancelled(true);
			return;
		}

		// Add mob
		dgr.Mobs.put(mob.getUniqueId(), mob);
		System.out.println("New mob spawn");
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onCreatureDespawn(ItemDespawnEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		DungeonManager.HandleMobKill((LivingEntity) event.getEntity());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		DungeonManager.HandleMobKill((LivingEntity) event.getEntity());
	}

}

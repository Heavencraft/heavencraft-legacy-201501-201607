package fr.heavencraft.rpg.zones;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;

import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.mobs.MobManager;

public class ZoneListener implements Listener
{

	public ZoneListener()
	{
		Bukkit.getPluginManager().registerEvents(this, HeavenRPG.getInstance());
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event)
	{
		if (!(event.getEntity() instanceof LivingEntity))
			return;

		LivingEntity let = event.getEntity();
		if (!(let.getType() == EntityType.CREEPER || let.getType() == EntityType.SKELETON
				|| let.getType() == EntityType.SPIDER || let.getType() == EntityType.ZOMBIE))
			return;

		if (ZoneManager.getZone(event.getEntity().getLocation()) != null)
		{
			Zone zone = ZoneManager.getZone(event.getEntity().getLocation());
			MobManager.createMob(event.getEntity(), zone);
			ZoneManager.getZoneByName(MobManager.getRPGMob(let).getSpawnZone());
			MobManager.showMobHealthBar(MobManager.getRPGMob(event.getEntity()));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityRegain(EntityRegainHealthEvent event)
	{
		Entity entity = event.getEntity();

		if (!(entity instanceof LivingEntity))
			return;
		if (entity instanceof Player)
			return;

		if (entity instanceof LivingEntity)
		{
			MobManager.showMobHealthBar(MobManager.getRPGMob((LivingEntity) entity));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onEntityDamageEvent(EntityDamageEvent event)
	{
		Entity entity = event.getEntity();
		if (!(entity instanceof LivingEntity))
			return;
		if (entity instanceof Player)
			return;
		LivingEntity living = (LivingEntity) entity;

		if (living.getNoDamageTicks() > living.getMaximumNoDamageTicks() / 2F)
			return;

		MobManager.showMobHealthBar(MobManager.getRPGMob(living));
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (event.getEntity() instanceof LivingEntity)
		{
			MobManager.hideBar(MobManager.getRPGMob(event.getEntity()));
		}
	}

}

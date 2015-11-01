package fr.heavencraft.rpg.mobs;

import org.bukkit.entity.LivingEntity;

import fr.heavencraft.rpg.zones.Zone;

public class RPGMob {
	private LivingEntity mob;
	private int level;
	private String spawnZone;
	private String spawningName;
	
	public RPGMob(LivingEntity entity, int level, Zone zone)
	{
		this.mob = entity;
		this.level = level;
		this.setSpawningName(entity.getCustomName());
		this.setSpawnZone(zone.getUniqueName());
	}
	
	public LivingEntity getMob() {
		return mob;
	}
	public void setMob(LivingEntity mob) {
		this.mob = mob;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	public String getSpawnZone() {
		return spawnZone;
	}

	public void setSpawnZone(String spawnZone) {
		this.spawnZone = spawnZone;
	}

	public String getSpawningName()
	{
		return spawningName;
	}

	public void setSpawningName(String spawningName)
	{
		this.spawningName = spawningName;
	}
	
}

package fr.hc.quest.npc;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import fr.heavencraft.heavencore.BukkitTickCounterTask;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.npc.NPC;

public abstract class HeavenNPC
{
	private static final int DEFAULT_ATTACK_COOLDOWN = 20; // 1 second

	private final int attackCooldown = DEFAULT_ATTACK_COOLDOWN;
	private long canAttackTick = 0;

	protected final NPC npc;
	private Team team;

	protected HeavenNPC(EntityType type, String name)
	{
		this.npc = CitizensAPI.getNPCRegistry().createNPC(type, name);
		HeavenNPCRegistry.get().register(this, npc);
	}

	public Team getTeam()
	{
		return team;
	}

	protected void setTeam(Team team)
	{
		this.team = team;
	}

	public boolean canAttack()
	{
		return BukkitTickCounterTask.getCounter() > canAttackTick;
	}

	public void onAttackPerformed()
	{
		canAttackTick = BukkitTickCounterTask.getCounter() + attackCooldown;
	}

	/*
	 * Citizens wrapping
	 */

	public boolean isSpawned()
	{
		return npc.isSpawned();
	}

	public Navigator getNavigator()
	{
		return npc.getNavigator();
	}

	public Entity getEntity()
	{
		return npc.getEntity();
	}
	
	/**
	 * Deletes the NPC permanently
	 */
	public void remove() {
		HeavenNPCRegistry.get().removeNPC(npc);
		npc.destroy();
	}
}

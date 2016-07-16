package fr.hc.quest.goals;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;

public abstract class AttackGoal implements Goal
{
	// For optimization purpose, use these location when calling Player.getLocation
	protected static Location NPC_LOCATION = new Location(null, 0, 0, 0);
	protected static Location TARGET_LOCATION = new Location(null, 0, 0, 0);

	protected static final int RADIUS = 16;

	protected final NPC npc;
	private Entity target;

	protected AttackGoal(NPC npc)
	{
		this.npc = npc;
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		if (!npc.isSpawned())
			return false;

		for (final Entity entity : npc.getEntity().getNearbyEntities(RADIUS, RADIUS, RADIUS))
		{
			if (canAttack(entity))
			{
				target = entity;
				return true;
			}
		}

		return false;
	}

	@Override
	public void run(GoalSelector selector)
	{
		attack(target);
		selector.finish();
	}

	@Override
	public void reset()
	{
		target = null;
	}

	protected abstract boolean canAttack(Entity target);

	protected abstract void attack(Entity target);
}
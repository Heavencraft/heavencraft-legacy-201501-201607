package fr.hc.quest.goals;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import fr.hc.quest.npc.HeavenNPC;
import net.citizensnpcs.api.ai.GoalSelector;

public abstract class AbstractAttackGoal extends AbstractGoal
{
	protected static Location TARGET_LOCATION = new Location(null, 0, 0, 0);

	protected final int radius;
	private final TargetAcceptor targetAcceptor;
	private Entity target;

	protected AbstractAttackGoal(HeavenNPC npc, int radius, TargetAcceptor targetAcceptor)
	{
		super(npc);
		this.radius = radius;
		this.targetAcceptor = targetAcceptor;
	}

	@Override
	public void reset()
	{
		super.reset();
		target = null;
	}

	@Override
	protected final void run(GoalSelector selector, int frame)
	{
		if (!npc.isSpawned())
			selector.finish();

		run(selector, frame, target);
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		if (!npc.isSpawned())
			return false;

		for (final Entity entity : npc.getEntity().getNearbyEntities(radius, radius, radius))
		{
			if (!targetAcceptor.accept(entity))
				continue;

			if (!canAttack(entity))
				continue;

			target = entity;
			return true;
		}

		return false;
	}

	protected abstract boolean canAttack(Entity target);

	protected abstract void run(GoalSelector selector, int frame, Entity target);
}
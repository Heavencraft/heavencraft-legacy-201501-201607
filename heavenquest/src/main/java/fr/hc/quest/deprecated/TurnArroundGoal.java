package fr.hc.quest.deprecated;

import org.bukkit.Location;

import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;

public class TurnArroundGoal implements Goal
{
	private static final double ANGLE_STEP = 10;

	private final NPC npc;
	private final Location center;
	private final double radius;
	private double angle;

	public TurnArroundGoal(NPC npc, Location center, double radius, double initialAngle)
	{
		this.npc = npc;
		this.center = center;
		this.radius = radius;
		this.angle = initialAngle;
	}

	@Override
	public void run(GoalSelector selector)
	{
		angle = (angle + ANGLE_STEP) % 360;
		final double deltaX = radius * Math.cos(Math.toRadians(angle));
		final double deltaZ = radius * Math.sin(Math.toRadians(angle));

		npc.getNavigator().setTarget(
				new Location(center.getWorld(), center.getX() + deltaX, center.getY(), center.getZ() + deltaZ));

		selector.finish();
	}

	@Override
	public void reset()
	{
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		return npc.isSpawned() && !npc.getNavigator().isNavigating();
	}
}
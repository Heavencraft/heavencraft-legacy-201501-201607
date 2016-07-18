package fr.hc.quest.deprecated;

import org.bukkit.Location;

import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;

public class StraightToLocationGoal implements Goal
{
	private final NPC npc;
	private final Location location;

	public StraightToLocationGoal(NPC npc, Location location)
	{
		this.npc = npc;
		this.location = location;
	}

	@Override
	public void reset()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void run(GoalSelector selector)
	{
		npc.getNavigator().setTarget(location);
		selector.finishAndRemove();
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		return npc.isSpawned() && !npc.getNavigator().isNavigating();
	}
}
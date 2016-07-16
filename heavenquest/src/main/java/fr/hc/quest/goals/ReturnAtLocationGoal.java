package fr.hc.quest.goals;

import org.bukkit.Location;

import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;

public class ReturnAtLocationGoal implements Goal
{

	private final NPC npc;
	private final Location location;

	public ReturnAtLocationGoal(NPC npc, Location location)
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
		selector.finish();
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		return npc.isSpawned() && !npc.getNavigator().isNavigating();
	}
}
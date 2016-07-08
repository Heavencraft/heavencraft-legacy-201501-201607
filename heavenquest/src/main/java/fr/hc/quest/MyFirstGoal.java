package fr.hc.quest;

import org.bukkit.entity.Player;

import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;
import net.citizensnpcs.api.npc.NPC;

public class MyFirstGoal implements Goal
{

	private final NPC npc;
	private final Player player;

	public MyFirstGoal(NPC npc, Player player)
	{
		this.npc = npc;
		this.player = player;
		this.npc.addTrait(NpcListener.class);
	}

	@Override
	public void reset()
	{

	}

	@Override
	public void run(GoalSelector selector)
	{
		npc.getNavigator().setTarget(player.getLocation());
		// npc.getNavigator().setPaused(false);
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{

		return npc.isSpawned() && !npc.getNavigator().isNavigating();
	}

}

package fr.hc.quest.goals;

import org.bukkit.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.hc.quest.npc.HeavenNPC;
import net.citizensnpcs.api.ai.Goal;
import net.citizensnpcs.api.ai.GoalSelector;

public abstract class AbstractGoal implements Goal
{
	protected static Location NPC_LOCATION = new Location(null, 0, 0, 0);

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final HeavenNPC npc;

	private int frame;

	protected AbstractGoal(HeavenNPC npc)
	{
		this.npc = npc;
	}

	@Override
	public void reset()
	{
		frame = 0;
	}

	@Override
	public final void run(GoalSelector selector)
	{
		run(selector, frame++);
	}

	protected abstract void run(GoalSelector selector, int frame);
}
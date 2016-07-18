package fr.hc.quest.goals;

import org.bukkit.Location;

import fr.hc.quest.npc.HeavenNPC;
import net.citizensnpcs.api.ai.GoalSelector;

public class GoToLocationGoal extends AbstractGoal
{
	private final Location targetLocation;

	public GoToLocationGoal(HeavenNPC npc, Location targetLocation)
	{
		super(npc);

		this.targetLocation = targetLocation;
	}

	@Override
	public boolean shouldExecute(GoalSelector selector)
	{
		if (!npc.isSpawned() || npc.getNavigator().isNavigating())
			return false;

		final Location npcLocation = npc.getEntity().getLocation(NPC_LOCATION);
		if (npcLocation.distance(targetLocation) <= 1.5)
			return false;

		npc.getNavigator().setTarget(targetLocation);
		return true;
	}

	@Override
	protected void run(GoalSelector selector, int frame)
	{
		if (!npc.getNavigator().isNavigating())
			selector.finish();

		// log.info("run");
		//
		// if (!npc.isSpawned())
		// {
		// log.info("!npc.isSpawned()");
		// selector.finish();
		// return;
		// }
		//
		// if (npc.getNavigator().isPaused())
		// {
		// npc.getNavigator().setPaused(false);
		// }
		//
		// if (npc.getNavigator().isNavigating())
		// {
		// log.info("npc.getNavigator().isNavigating()");
		// return;
		// }
		//
		// final Location npcLocation = npc.getEntity().getLocation(NPC_LOCATION);
		// if (npcLocation.distance(targetLocation) <= 1)
		// {
		// log.info("npcLocation.distance(targetLocation) <= 1");
		// selector.finish();
		// return;
		// }
		//
		// npc.getNavigator().setTarget(targetLocation);
		// log.info("npc.getNavigator().setTarget(targetLocation);");
	}
}
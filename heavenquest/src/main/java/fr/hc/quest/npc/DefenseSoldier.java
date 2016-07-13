package fr.hc.quest.npc;

import org.bukkit.Location;

import fr.hc.quest.TargetNearbyNPCBehavior;
import fr.hc.quest.strategy.DefenseStrategy;
import net.citizensnpcs.api.ai.goals.MoveToGoal;

/*
 * NPC that move to a point and defend it until it dies, then respawn
 */
public class DefenseSoldier extends AbstractSoldier
{
	public DefenseSoldier(Location spawnLocation, Location defenseLocation)
	{
		super("DefenseSoldier", spawnLocation, Team.CITADEL);

		npc.getNavigator().getLocalParameters().attackRange(25);
		npc.getNavigator().getLocalParameters().attackStrategy(new DefenseStrategy());

		npc.getDefaultGoalController().addBehavior(new MoveToGoal(npc, defenseLocation), 1);
		npc.getDefaultGoalController().addBehavior(new TargetNearbyNPCBehavior(npc), 2);
	}
}
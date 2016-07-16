package fr.hc.quest.npc;

import org.bukkit.Location;

import fr.hc.quest.goals.ShootBowGoal;
import fr.hc.quest.strategy.DefenseStrategy;

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

		// npc.getDefaultGoalController().addBehavior(new MoveToGoal(npc, defenseLocation), 1);
		// npc.getDefaultGoalController().addBehavior(new TargetNearbyNPCBehavior(npc), 2);

		// npc.getDefaultGoalController().addGoal(new ReturnAtLocationGoal(npc, defenseLocation), 1);
		// npc.getDefaultGoalController().addGoal(new LookAtGoal(npc), 2);
		npc.getDefaultGoalController().addGoal(new ShootBowGoal(npc), 2);
	}
}
package fr.hc.quest.npc;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.hc.quest.TargetNearbyNPCBehavior;
import fr.hc.quest.strategy.DefenseStrategy;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.goals.MoveToGoal;
import net.citizensnpcs.api.npc.NPC;

/*
 * NPC that move to a point and defend it until it dies, then respawn
 */
public class DefenseSoldier
{
	private final NPC npc;

	public DefenseSoldier(Location spawnLocation, Location defenseLocation)
	{
		npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Soldier");
		npc.data().set("team", "citadel");

		npc.getNavigator().getLocalParameters().attackRange(25);
		npc.getNavigator().getLocalParameters().attackStrategy(new DefenseStrategy());

		npc.getDefaultGoalController().addBehavior(new MoveToGoal(npc, defenseLocation), 1);
		npc.getDefaultGoalController().addBehavior(new TargetNearbyNPCBehavior(npc), 2);

		npc.spawn(spawnLocation);
		npc.setProtected(false);
	}
}
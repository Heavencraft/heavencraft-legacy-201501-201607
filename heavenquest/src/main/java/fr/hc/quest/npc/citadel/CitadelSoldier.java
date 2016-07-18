package fr.hc.quest.npc.citadel;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.hc.quest.goals.GoToLocationGoal;
import fr.hc.quest.goals.ShootBowGoal;
import fr.hc.quest.goals.SwordAttackGoal;
import fr.hc.quest.npc.HeavenNPC;
import fr.hc.quest.npc.Team;

public class CitadelSoldier extends HeavenNPC
{
	public CitadelSoldier(String name, Location location, Location spawnLocation)
	{
		super(EntityType.PLAYER, name);
		setTeam(Team.CITADEL);

		npc.setProtected(false);
		npc.setFlyable(false);
		npc.spawn(spawnLocation);

		npc.getDefaultGoalController().addGoal(new GoToLocationGoal(this, location), 1);
		npc.getDefaultGoalController().addGoal(new ShootBowGoal(this, 20, CitadelTargetAcceptor.INSTANCE), 2);
		npc.getDefaultGoalController().addGoal(new SwordAttackGoal(this, 3, CitadelTargetAcceptor.INSTANCE), 3);
	}
}
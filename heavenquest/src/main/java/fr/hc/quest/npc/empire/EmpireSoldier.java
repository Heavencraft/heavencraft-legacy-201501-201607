package fr.hc.quest.npc.empire;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.hc.quest.goals.GoToLocationGoal;
import fr.hc.quest.goals.ShootBowGoal;
import fr.hc.quest.goals.SwordAttackGoal;
import fr.hc.quest.npc.HeavenNPC;
import fr.hc.quest.npc.Team;

public class EmpireSoldier extends HeavenNPC
{
	public EmpireSoldier(String name, Location location, Location spawnLocation)
	{
		super(EntityType.PLAYER, name);
		setTeam(Team.EMPIRE);

		npc.setProtected(false);
		npc.setFlyable(false);
		npc.spawn(spawnLocation);
		npc.getNavigator().getLocalParameters().useNewPathfinder(true);

		npc.getDefaultGoalController().addGoal(new GoToLocationGoal(this, location), 1);
		npc.getDefaultGoalController().addGoal(new ShootBowGoal(this, 16, EmpireTargetAcceptor.INSTANCE), 2);
		npc.getDefaultGoalController().addGoal(new SwordAttackGoal(this, 8, EmpireTargetAcceptor.INSTANCE), 3);
	}
}
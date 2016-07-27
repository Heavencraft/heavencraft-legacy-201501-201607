package fr.hc.quest.npc.borderpatrol;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import fr.hc.quest.goals.SwordAttackGoal;
import fr.hc.quest.npc.HeavenNPC;
import fr.hc.quest.npc.HeavenNPCRegistry;
import fr.hc.quest.npc.citadel.CitadelTargetAcceptor;
import fr.hc.quest.npc.empire.EmpireTargetAcceptor;

public class BorderPatrol extends HeavenNPC
{

	protected BorderPatrol(String name, Location spawnLocation)
	{
		super(EntityType.PLAYER, name);
		//setTeam(Team.EMPIRE);
		npc.setProtected(false);
		npc.setFlyable(false);
		npc.spawn(spawnLocation);
		
		npc.getDefaultGoalController().addGoal(new SwordAttackGoal(this, 3, CitadelTargetAcceptor.INSTANCE), 1);
		npc.getDefaultGoalController().addGoal(new SwordAttackGoal(this, 3, EmpireTargetAcceptor.INSTANCE), 2);
		
	}
	
	public void disposeNPC() {
		HeavenNPCRegistry.get().removeNPC(npc);
	}

}

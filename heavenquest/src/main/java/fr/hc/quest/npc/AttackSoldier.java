package fr.hc.quest.npc;

import org.bukkit.Location;

public class AttackSoldier extends AbstractSoldier
{
	public AttackSoldier(Location spawnLocation)
	{
		super("AttackSoldier", spawnLocation, Team.FAGGOTS);
	}
}
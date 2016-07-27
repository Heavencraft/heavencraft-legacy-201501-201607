package fr.hc.quest.npc.borderpatrol;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BorderPatrolListener
{
	
	private static List<BorderPatrol> patrolList = new ArrayList<BorderPatrol>();

	public BorderPatrolListener(){
		patrolList.add(new BorderPatrol("Rainbwo", new Location(Bukkit.getWorld("final_map"), 513, 134, 474)));
		patrolList.add(new BorderPatrol("Spakle", new Location(Bukkit.getWorld("final_map"), 490, 137, 504)));
		patrolList.add(new BorderPatrol("Futashy", new Location(Bukkit.getWorld("final_map"), 454, 121, 528)));
		patrolList.add(new BorderPatrol("Roarity", new Location(Bukkit.getWorld("final_map"), 427, 121, 528)));
		
	}

	public static List<BorderPatrol> getPatrolList()
	{
		return patrolList;
	}
	
}

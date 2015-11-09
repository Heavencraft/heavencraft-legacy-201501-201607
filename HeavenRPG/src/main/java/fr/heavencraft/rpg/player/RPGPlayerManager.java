package fr.heavencraft.rpg.player;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RPGPlayerManager {
	private static ArrayList<RPGPlayer> players = new ArrayList<RPGPlayer>();
	
	
	public static void createRPGPlayer(RPGPlayer p)
	{
		if(!players.contains(p))
			players.add(p);
	}
	
	public static RPGPlayer createRPGPlayer(Player p)
	{
		RPGPlayer rp = new RPGPlayer(p);
		if(!players.contains(rp))
			players.add(rp);
		return rp;
	}
	
	public static void removeRPGPlayer(String playerName) {
		for (RPGPlayer player : players)
		{
			if (player.getPlayer().getName().equalsIgnoreCase(playerName))
				players.remove(player);
		}
	}
	public static void removeRPGPlayer(RPGPlayer IP) {
		players.remove(IP);
	}

	/**
	 * Get NCPlayer
	 * 
	 * @param playername
	 */
	public static RPGPlayer getRPGPlayer(String playerName) {
		for (RPGPlayer IP : players)
		{
			if (IP.getPlayer().getName().equalsIgnoreCase(playerName))
				return IP;
		}
		return createRPGPlayer(Bukkit.getPlayer(playerName));
	}

	/**
	 * Create NCPlayer
	 * 
	 * @param Player
	 */
	public static RPGPlayer getRPGPlayer(Player p) {
		for (RPGPlayer IP : players)
		{
			if (IP.getPlayer() == p)
				return IP;
		}
		return createRPGPlayer(p);
	}
}

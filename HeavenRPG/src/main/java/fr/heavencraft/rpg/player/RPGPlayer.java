package fr.heavencraft.rpg.player;

import org.bukkit.entity.Player;

public class RPGPlayer {
	private Player player;
	private int rpgXP;
	private int reputation;
	
	public RPGPlayer(Player p)
	{
		this.player = p;
	} 
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	/**
	 * Retourne l'XP RPG que le joueur a.
	 * @return
	 */
	public int getRPGXp()
	{
		return this.rpgXP;
	}
	
	public void setRPGXP(int xp)
	{
		this.rpgXP = xp;
	}
	
	/**
	 * Retourne la réputation du joueur.
	 * @return
	 */
	public int getReputation()
	{
		return this.reputation;
	}
	
}

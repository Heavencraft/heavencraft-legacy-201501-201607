package fr.heavencraft.rpg.Parchemins;

import org.bukkit.inventory.ItemStack;

import fr.heavencraft.rpg.player.RPGPlayer;

public interface IParchemin {
	/**
	 * L'expérience nécessaire a l'execution du parchemin.
	 * @return
	 */
	int RPGexpieirence();
	
	/**
	 * Retourne si le joueur peut utiliser le parchemin
	 * @param player
	 * @return
	 */
	public boolean canDo(RPGPlayer player);
	
	/**
	 * Retourne l'item Parchemin correspondant
	 * @return
	 */
	public  ItemStack getItem();
	
	/**
	 * Execute le parchemin
	 * @param player
	 */
	public  void executeParchemin(RPGPlayer player);
}

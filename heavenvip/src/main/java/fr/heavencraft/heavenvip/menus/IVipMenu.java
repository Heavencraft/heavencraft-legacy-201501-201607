package fr.heavencraft.heavenvip.menus;

import org.bukkit.entity.Player;

public interface IVipMenu
{		
	/**
	 * Shows the menu
	 * (Does update already opened menus
	 * @param p
	 */
	public void openMenu(Player p);
	
	/**
	 * Reopens a menu, forcing a new interface.
	 * @param p
	 */
	public void openNewMenu(Player p);
}

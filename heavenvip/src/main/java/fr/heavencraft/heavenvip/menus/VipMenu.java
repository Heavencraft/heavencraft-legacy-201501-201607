package fr.heavencraft.heavenvip.menus;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;

public abstract class VipMenu implements IVipMenu{
	protected final Menu m;
	
	/**
	 * Constructor
	 * @param menu the menu
	 */
	public VipMenu(Menu menu) {
		this.m = menu;
	}
	
	public void OpenMenu(Player player) throws HeavenException {
		MenuAPI.openMenu(player, this.m);
		return;
	}
	
	public Menu GetMenu() {
		return this.m;
	}
}

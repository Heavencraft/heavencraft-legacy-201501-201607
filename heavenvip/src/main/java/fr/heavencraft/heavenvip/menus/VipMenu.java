package fr.heavencraft.heavenvip.menus;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
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
		Menu oldMenu = MenuAPI.getMenu(player);
		// If player has no menu open, open a new one
		if(oldMenu == null) {
			ChatUtil.broadcastMessage("OPENING NEW MENU"); //TODO remove
			MenuAPI.openMenu(player, this.m);
			return;
		}
		// Player has already an open menu, update it.
		ChatUtil.broadcastMessage("Refreshing"); //TODO remove
		oldMenu = this.m;
		oldMenu.refresh(player);
	}
	
	public void OpenNewMenu(Player player) throws HeavenException {
		MenuAPI.openMenu(player, this.m);
	}
	
	public Menu GetMenu() {
		return this.m;
	}
}

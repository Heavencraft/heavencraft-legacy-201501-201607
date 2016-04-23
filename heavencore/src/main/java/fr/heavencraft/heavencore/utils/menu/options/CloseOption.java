package fr.heavencraft.heavencore.utils.menu.options;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;

public class CloseOption extends Option {

	public CloseOption() {
		super(Material.WOOL, (short) 14, "§cClose", "§f§oCloses the menu");
	}
	
	@Override
	public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException {
		MenuAPI.closeMenu(player);
	}

}

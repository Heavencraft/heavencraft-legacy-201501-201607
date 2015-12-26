package fr.heavencraft.heavencore.utils.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface ClickHandler {

	public boolean onClick(Player player, ItemStack cursor, ItemStack current, ClickType type);
	
}

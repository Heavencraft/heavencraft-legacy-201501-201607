package fr.heavencraft.heavencore.utils.menu.options;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;

public class EmptyOption extends Option {
	
	public EmptyOption(Material type) {
		super(type, "§r");
	}
	
	public EmptyOption(Material type, short damage) {
		this(type);
		super.setDamage(damage);
	}
	
	public EmptyOption(Material type, short damage, int amount) {
		this(type, damage);
		super.setAmount(amount);
	}
	
	@Override
	public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) {
		;
	}

}

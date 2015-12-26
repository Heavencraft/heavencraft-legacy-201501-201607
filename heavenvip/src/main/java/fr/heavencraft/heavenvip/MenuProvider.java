package fr.heavencraft.heavenvip;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavencore.utils.menu.options.Option;

public class MenuProvider
{
	
	public Menu getMainVIPMenu(Player p) {
		Menu menu = new Menu("§7Aventages VIP -- HPS", 1);
		
		// Particles
		menu.addOption(0, 0, new Option(Material.NETHER_STAR, ChatColor.GOLD + "Particules") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException
			{
				// Open the particle sub-menu
				MenuAPI.closeMenu(p);
				MenuAPI.openMenu(player, getParticleMenu(p));
			}
		});
		
		return menu;
	}
	
	public Menu getParticleMenu(Player p) {
		return null;
	}

}

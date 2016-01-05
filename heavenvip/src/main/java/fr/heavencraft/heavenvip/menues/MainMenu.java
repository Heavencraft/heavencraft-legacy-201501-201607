package fr.heavencraft.heavenvip.menues;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.options.Option;

public class MainMenu implements IVipMenu
{
	@Override
	public Menu getMenu(Player p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VipMenuItem> getOptions(Player p)
	{
		Collection<VipMenuItem> coll = new ArrayList<VipMenuItem>();
		
		// Particles
		coll.add(new VipMenuItem(3, 0, 
				new Option(Material.NETHER_STAR, ChatColor.GOLD + "Particules") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				// Open the particle sub-menu
				ChatUtil.broadcastMessage("Open Particle menu");
			}
			
		}));
		
		// Heads
		coll.add(new VipMenuItem(5, 0, 
				new Option(Material.SKULL_ITEM, (short)3, 1, ChatColor.GOLD + "Tetes du Staff") {
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				// Open the particle sub-menu
				ChatUtil.broadcastMessage("Open Heads");
			}
			
		}));
		return coll;
	}

}

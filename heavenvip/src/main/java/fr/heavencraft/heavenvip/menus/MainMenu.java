package fr.heavencraft.heavenvip.menus;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.options.Option;

public class MainMenu extends VipMenu
{

	public MainMenu()
	{
		super(new Menu("§6Aventages VIP -- HPS", 3));
		super.m.addOption(3, 1, new Option(Material.NETHER_STAR, ChatColor.GOLD + "Particules")
		{
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				ParticlePackMenu particleMenu = new ParticlePackMenu(player);
				particleMenu.openMenu(player);
			}
		});
		super.m.addOption(4, 1, new Option(Material.SKULL_ITEM, (short)3, ChatColor.GOLD + "Têtes du Staff")
		{
			@Override
			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
					throws HeavenException
			{
				HeadMenu hmenu = new HeadMenu(player);
				hmenu.openMenu(player);
			}
		});
		
//		super.m.addOption(5, 1, new Option(Material.ANVIL, ChatColor.GOLD + "TEST")
//		{
//			@Override
//			public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
//					throws HeavenException
//			{
//				ChatUtil.broadcastMessage("OPEN TEST MENU LEL");
//			}
//		});
	}

	@Override
	public void openMenu(Player p)
	{
		try
		{
			super.OpenMenu(p);
		}
		catch (HeavenException e)
		{
			e.printStackTrace();
		}

	}
}

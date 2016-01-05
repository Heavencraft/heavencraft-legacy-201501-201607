package fr.heavencraft.heavenvip;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;
import fr.heavencraft.heavencore.utils.menu.options.Option;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenvip.menues.VipMenuItem;
import fr.heavencraft.heavenvip.querys.UnequipEffectQuery;
import fr.heavencraft.heavenvip.querys.UpdateEquipedEffectsQuery;

public class MenuUtils
{
	
	
	public static void openMenu(VipMenuItem vipItem, Player p) 
	{
		Menu m = MenuAPI.getMenu(p);
		if(m == null) {
			m = new Menu()
		}
	}
	
	/**
	 * Generates a default Lore for Store Items
	 * @param title
	 * @param price
	 * @param description Each line is separated by ";"
	 * @return
	 */
	public static String[] generateShopItemLore(String title, int price, String description) 
	{
		List<String> res = new ArrayList<String>();

		// Lore title
		res.add(ChatColor.BOLD + ChatColor.translateAlternateColorCodes('§', title));
		// Lore description
		String[] splittedData = description.split(";");
		for(int i = 0; i< splittedData.length; i++)
		{
			res.add(ChatColor.translateAlternateColorCodes('§', splittedData[i]));
		}
		// Lore spare line
		res.add("");
		// Cost
		res.add(ChatColor.UNDERLINE + "" + ChatColor.AQUA + "Prix: " + price + " HPS");

		return res.toArray(new String[0]);
	}

	/**
	 * Generates a default Lore for an effect
	 * @param title
	 * @param description
	 * @return
	 */
	public static String[] generateEffectDescriptionLore(String title, String description) 
	{
		List<String> res = new ArrayList<String>();

		// Lore title
		res.add(ChatColor.BOLD + ChatColor.translateAlternateColorCodes('§', title));
		// Lore description
		String[] splittedData = description.split(";");
		for(int i = 0; i< splittedData.length; i++)
		{
			res.add(ChatColor.translateAlternateColorCodes('§', splittedData[i]));
		}

		return res.toArray(new String[0]);
	}

	/**
	 * Adds the Navigation Bar to actualMenu
	 * @param actualMenu Menu where we want the NavBar
	 * @param lastMenu menu to get back
	 * @param p Player
	 * @param y Line where to put Navigation Bar
	 */
	public static void attachNavigationBar(Menu actualMenu, Menu lastMenu, Player p, int y) 
	{
		// Check if we are in bound for the y coordinate
		if(actualMenu.getHeight() <= y) {
			actualMenu.extendLines(y+1);
		}
		// Back button
		if(lastMenu != null)
			actualMenu.addOption(0, y, new Option(Material.ARROW, "Précédent")
			{ 
				@Override
				public void onClick(Menu menu, Player player, ItemStack cursor,ItemStack current, ClickType type)
						throws HeavenException {			
					MenuAPI.closeMenu(player);
					MenuAPI.openMenu(player, lastMenu);
				}
			});

		// HPS count
		int hpsCount;
		try
		{
			hpsCount = HpsManager.getBalance(p.getName());
			actualMenu.addOption(8, y, new Option(Material.GOLD_INGOT, "Mes HPS: " + ChatColor.YELLOW + hpsCount) {
				@Override
				public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
						throws HeavenException
				{}
			});
		}
		catch (HeavenException e)
		{
			actualMenu.addOption(8, y, new Option(Material.GOLD_INGOT, "Mes HPS: " + ChatColor.YELLOW + "0", e.getMessage()) {
				@Override
				public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type)
						throws HeavenException
				{}
			});
		}
	}

	/**
	 * Add a player equipment
	 * @param effectType ex: 'p' for particle.
	 * @param p Player
	 * @param effectId id of the effect
	 */
	public static void equipEffect(char effectType, Player p, int effectId, IMenuUpdateCallback callback)
	{
		ChatUtil.broadcastMessage("EQUIP :" + effectType +  " player: " + p.getName() +  " effect: " + effectId);
		QueriesHandler.addQuery(new UpdateEquipedEffectsQuery(effectType, PlayerUtil.getUUID(p), effectId){
			@Override
			public void onSuccess()
			{
				callback.success();
			}
			@Override
			public void onHeavenException(HeavenException ex)
			{
				try
				{
					MenuAPI.closeMenu(p);
				}
				catch (HeavenException e)
				{
				}
				ChatUtil.sendMessage(p, ex.getMessage());
				callback.fail();
			}
		});
	}
	
	/**
	 * Remove equipment
	 * @param effectType
	 * @param p
	 * @param effectId
	 */
	public static void unequipEffect(char effectType, Player p, int effectId, IMenuUpdateCallback callback)
	{
		QueriesHandler.addQuery(new UnequipEffectQuery(effectType, PlayerUtil.getUUID(p), effectId){
			@Override
			public void onSuccess()
			{
				callback.success();
			}
			@Override
			public void onHeavenException(HeavenException ex)
			{
				try
				{
					MenuAPI.closeMenu(p);
				}
				catch (HeavenException e)
				{
				}
				ChatUtil.sendMessage(p, ex.getMessage());
				callback.fail();
			}
		});
	}
	
	public interface IMenuUpdateCallback
	{
	    public void success ();
	    public void fail ();
	}
}

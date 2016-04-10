package fr.heavencraft.heavenrp.scrolls;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public abstract class Scroll
{

	final String name;
	final List<String> lore;

	protected Scroll(String name, List<String> lore)
	{
		super();
		for(int i = 0; i < lore.size(); i++)
			lore.set(i, ChatColor.translateAlternateColorCodes('&', lore.get(i)));
		this.name = name;
		this.lore = lore;
	}

	/**
	 * Returns the scroll item
	 * 
	 * @return
	 */
	public ItemStack getItem()
	{
		ItemStack scroll = new ItemStack(Material.PAPER);
		ItemMeta meta = scroll.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + this.name);
		meta.setLore(lore);
		scroll.setItemMeta(meta);
		return scroll;
	}

	/**
	 * Execute the scroll
	 * 
	 * @param player
	 * @throws HeavenException
	 */
	public void executeScroll(Player player) throws HeavenException
	{

	}
}

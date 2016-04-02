package fr.heavencraft.structureblock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class StructureBlockSmelteryListener implements Listener
{
	final StructureBlock plugin;

	// smeltery properties
	final Vector smelterySize;
	final Vector relativeVector = new Vector(-4, 2, 2);

	// basic item
	final ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 1);

	public StructureBlockSmelteryListener(StructureBlock plugin, Vector smelterySize)
	{
		this.smelterySize = smelterySize;
		this.plugin = plugin;

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
		{
			return;
		}
		Block clickedBlock = event.getClickedBlock();
		if (clickedBlock.getType() != Material.DROPPER)
		{
			return;
		}
		if (StructureBlockAnalyzer.checkStructure(event.getPlayer(), event.getBlockFace(),
				clickedBlock.getLocation(), relativeVector, smelterySize, plugin.smelteryLayers) == false)
		{
			return;
		}
		event.setCancelled(true);
		event.getPlayer().openInventory(getInventory());
	}

	/**
	 * construct the smeltery inventory
	 * 
	 * @return
	 */
	private Inventory getInventory()
	{
		Inventory smelteryInventory = Bukkit.createInventory(null, 27, ChatColor.RED + "              Fonderie");
		for (int i = 0; i < 9; i++)
		{
			smelteryInventory.setItem(i, obsidian);
			smelteryInventory.setItem(i + 18, obsidian);
		}
		smelteryInventory.setItem(9, obsidian);
		smelteryInventory.setItem(12, obsidian);
		smelteryInventory.setItem(14, obsidian);
		smelteryInventory.setItem(17, obsidian);

		ItemStack cauldron = new ItemStack(Material.CAULDRON_ITEM, 1);
		ItemMeta cauldronMeta = cauldron.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.GRAY + "Cliquez pour faire fondre");
		lore.add(ChatColor.GRAY + "(Consomme un seau de lave)");
		cauldronMeta.setLore(lore);
		cauldronMeta.setDisplayName(ChatColor.RED + "Faire Fondre");
		cauldron.setItemMeta(cauldronMeta);
		smelteryInventory.setItem(13, cauldron);

		return smelteryInventory;
	}
}

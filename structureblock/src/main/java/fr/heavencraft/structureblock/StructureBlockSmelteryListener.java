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
	// define properties
	final StructureBlock plugin;
	final Vector smelterySize;
	final Vector relativeVector = new Vector(-4, 2, 2);

	// define error
	final String jobsError = ChatColor.RED + "Vous devez être Ferrailleur ou Forgeron pour utiliser la fonderie.";

	// define valid jobs list
	final ArrayList<Integer> jobsList = new ArrayList();

	public StructureBlockSmelteryListener(StructureBlock plugin, Vector smelterySize)
	{
		this.smelterySize = smelterySize;
		this.plugin = plugin;

		// fill list with jobs id
		jobsList.add(9);
		jobsList.add(20);

		Bukkit.getPluginManager().registerEvents(this, plugin);

	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		final Block clickedBlock = event.getClickedBlock();

		if (clickedBlock.getType() != Material.DROPPER)
			return;

		if (StructureBlockAnalyzer.testStructure(event.getPlayer(), event.getBlockFace(),
				clickedBlock.getLocation(), relativeVector, smelterySize, plugin.smelteryLayers) == false)
		{
			return;
		}

		// cancel event and open the smeltery inventory
		event.setCancelled(true);

		// final Job job = user.getJob();
		// if (job == null || jobsList.contains(job) ){
		// event.getPlayer().sendMessage(jobsError);
		// return;
		// }
		event.getPlayer().openInventory(defineInventory());

	}

	private Inventory defineInventory()
	{
		Inventory smelteryInventory = Bukkit.createInventory(null, 27, ChatColor.RED + "              Fonderie");

		// create the outline of the smeltery
		for (int i = 0; i < 9; i++)
		{
			smelteryInventory.setItem(i, new ItemStack(Material.OBSIDIAN, 1));
			smelteryInventory.setItem(i + 18, new ItemStack(Material.OBSIDIAN, 1));
		}
		smelteryInventory.setItem(9, new ItemStack(Material.OBSIDIAN, 1));
		smelteryInventory.setItem(12, new ItemStack(Material.OBSIDIAN, 1));
		smelteryInventory.setItem(14, new ItemStack(Material.OBSIDIAN, 1));
		smelteryInventory.setItem(17, new ItemStack(Material.OBSIDIAN, 1));

		// create the "Push to Smelt" item
		ItemStack cauldron = new ItemStack(Material.CAULDRON_ITEM, 1);
		ItemMeta cauldronMeta = cauldron.getItemMeta();
		ArrayList<String> lore = new ArrayList<>();
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
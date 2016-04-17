package fr.heavencraft.heavenrp.structureblock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.jobs.Job;

public class StructureBlockSmelteryListener extends AbstractListener<HeavenPlugin>
{
	// smeltery properties
	final Vector relativeVector = new Vector(-4, 2, 2);
	final ArrayList<Integer> jobList = new ArrayList<Integer>();

	// basic item
	final ItemStack obsidian = new ItemStack(Material.OBSIDIAN, 1);

	// cauldron properties
	final String FIRSTLORE = ChatColor.GRAY + "Cliquez pour faire fondre";
	final String SECONDLORE = ChatColor.GRAY + "(Consomme un seau de lave)";
	final String DISPLAYNAME = ChatColor.RED + "Faire Fondre";
	final String NOVALIDJOB = "Vous devez être forgeron ou ferailleur pour utiliser cette machine.";

	public StructureBlockSmelteryListener(HeavenPlugin plugin)
	{
		super(plugin);
		jobList.add(9);
		jobList.add(11);

	}

	@EventHandler
	private void onPlayerInteract(PlayerInteractEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		final Block clickedBlock = event.getClickedBlock();
		if (clickedBlock.getType() != Material.DROPPER)
			return;

		if (StructureBlockAnalyzer.checkStructure(player, event.getBlockFace(), clickedBlock.getLocation(),
				relativeVector) == false)
			return;

		final User user = UserProvider.getUserByName(player.getName());
		final Job job = user.getJob();
		if (!jobList.contains(job.getId()))
		{
			ChatUtil.sendMessage(player, NOVALIDJOB);
			return;
		}

		event.setCancelled(true);
		player.openInventory(getInventory());
	}

	/**
	 * construct the smeltery inventory
	 * 
	 * @return
	 */
	private Inventory getInventory()
	{
		final Inventory smelteryInventory = Bukkit.createInventory(null, 27, ChatColor.RED + "              Fonderie");
		for (int i = 0; i < 9; i++)
		{
			smelteryInventory.setItem(i, obsidian);
			smelteryInventory.setItem(i + 18, obsidian);
		}
		smelteryInventory.setItem(9, obsidian);
		smelteryInventory.setItem(12, obsidian);
		smelteryInventory.setItem(14, obsidian);
		smelteryInventory.setItem(17, obsidian);

		final ItemStack cauldron = new ItemStack(Material.CAULDRON_ITEM, 1);
		final ItemMeta cauldronMeta = cauldron.getItemMeta();
		final ArrayList<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(FIRSTLORE);
		lore.add(SECONDLORE);
		cauldronMeta.setLore(lore);
		cauldronMeta.setDisplayName(DISPLAYNAME);
		cauldron.setItemMeta(cauldronMeta);
		smelteryInventory.setItem(13, cauldron);

		return smelteryInventory;
	}
}

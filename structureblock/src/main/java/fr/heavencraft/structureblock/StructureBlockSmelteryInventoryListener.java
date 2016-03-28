package fr.heavencraft.structureblock;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class StructureBlockSmelteryInventoryListener implements Listener
{
	private final String inventoryTitle = ChatColor.RED + "              Fonderie";

	// smeltery message
	private final String invalidPlaceMessage = ChatColor.RED
			+ "Ceci est la zone de récuperation des ressources. Vous ne pouvez pas placer d'objets dans cette zone.";
	private final String lavaError = ChatColor.RED
			+ "Il vous faut un seau de lave pour faire fonctionner la fonderie.";
	private final String invalidItemError = ChatColor.RED
			+ "Certains des objets que vous voulez faire fondre ne sont pas valide, veuillez les retirer.";
	private final String nothingError = ChatColor.RED + "Il n'y a rien à faire fondre.";
	private final String smelterySuccess = ChatColor.DARK_GREEN + "Vous avez bien fais fondre vos Objets !";
	private final String withdrawFullError = ChatColor.RED + "Il y a déjà des matériaux à récuperer.";
	private final String insufficientError = ChatColor.RED
			+ "Au moins un des Objet que vous avez déposer ne vous rapportera rien. Corriger cela avant de faire fonctionner la fonderie.";

	// slot and action list
	private final ArrayList<Integer> slotTakeList = new ArrayList<>();
	private final ArrayList<Integer> slotPutList = new ArrayList<>();
	private final ArrayList<InventoryAction> placeAction = new ArrayList<>();

	// material to smelt list
	private final ArrayList<Material> smeltList = new ArrayList<>();
	private final ArrayList<Material> insufficientList = new ArrayList<>();
	private final ArrayList<Material> ironList = new ArrayList<>();
	private final ArrayList<Material> goldList = new ArrayList<>();
	private final ArrayList<Material> diamondList = new ArrayList<>();

	private final ItemStack air = new ItemStack(Material.AIR);

	public StructureBlockSmelteryInventoryListener(StructureBlock plugin)
	{
		// fill slot list
		slotPutList.add(10);
		slotPutList.add(11);
		slotTakeList.add(15);
		slotTakeList.add(16);

		// fill action list
		placeAction.add(InventoryAction.HOTBAR_SWAP);
		placeAction.add(InventoryAction.MOVE_TO_OTHER_INVENTORY);
		placeAction.add(InventoryAction.PLACE_ALL);
		placeAction.add(InventoryAction.PLACE_SOME);
		placeAction.add(InventoryAction.PLACE_ONE);
		placeAction.add(InventoryAction.SWAP_WITH_CURSOR);

		// fill material to smelt list
		ironList.add(Material.IRON_AXE);
		ironList.add(Material.IRON_PICKAXE);
		ironList.add(Material.IRON_SWORD);
		ironList.add(Material.IRON_CHESTPLATE);
		ironList.add(Material.IRON_LEGGINGS);
		ironList.add(Material.IRON_BOOTS);
		ironList.add(Material.IRON_HELMET);
		ironList.add(Material.IRON_BARDING);
		insufficientList.add(Material.IRON_SPADE);
		insufficientList.add(Material.IRON_HOE);

		goldList.add(Material.GOLD_AXE);
		goldList.add(Material.GOLD_PICKAXE);
		goldList.add(Material.GOLD_SWORD);
		goldList.add(Material.GOLD_CHESTPLATE);
		goldList.add(Material.GOLD_LEGGINGS);
		goldList.add(Material.GOLD_BOOTS);
		goldList.add(Material.GOLD_HELMET);
		goldList.add(Material.GOLD_BARDING);
		insufficientList.add(Material.GOLD_SPADE);
		insufficientList.add(Material.GOLD_HOE);

		diamondList.add(Material.DIAMOND_AXE);
		diamondList.add(Material.DIAMOND_PICKAXE);
		diamondList.add(Material.DIAMOND_SWORD);
		diamondList.add(Material.DIAMOND_CHESTPLATE);
		diamondList.add(Material.DIAMOND_LEGGINGS);
		diamondList.add(Material.DIAMOND_BOOTS);
		diamondList.add(Material.DIAMOND_HELMET);
		diamondList.add(Material.DIAMOND_BARDING);
		insufficientList.add(Material.DIAMOND_SPADE);
		insufficientList.add(Material.DIAMOND_HOE);

		smeltList.addAll(diamondList);
		smeltList.addAll(ironList);
		smeltList.addAll(goldList);
		smeltList.add(Material.AIR);

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	private void onPlayerInteractWithInventory(InventoryClickEvent event)
	{
		if (!event.getInventory().getName().equals(inventoryTitle))
			return;

		// cancelled dangerous action
		InventoryAction action = event.getAction();
		if (action == InventoryAction.COLLECT_TO_CURSOR)
		{
			event.setCancelled(true);
			return;
		}

		if (event.getRawSlot() >= 27)
		{
			if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY)
				event.setCancelled(true);
			return;
		}

		// test action in withdraw slot
		else if (slotTakeList.contains(event.getRawSlot()))
		{
			if (placeAction.contains(action))
			{
				event.getWhoClicked().sendMessage(invalidPlaceMessage);
				event.setCancelled(true);
			}
			return;
		}

		// test if player want to smelt
		else if (event.getRawSlot() == 13)
		{
			playerLaunchSmelt(event.getWhoClicked(), event.getInventory());
			event.setCancelled(true);
		}

		// test if player want to put item in smeltery
		else if (slotPutList.contains(event.getRawSlot()))
			return;

		else
			event.setCancelled(true);
	}

	@EventHandler
	private void onPlayerCloseInventory(InventoryCloseEvent event)
	{
		if (!event.getInventory().getName().equals(inventoryTitle))
			return;

		// test if there is some item left in the smeltery
		if (event.getInventory().getItem(10) != null)
		{
			event.getPlayer().getInventory().addItem(event.getInventory().getItem(10));
			event.getInventory().setItem(10, air);
		}
		if (event.getInventory().getItem(11) != null)
		{
			event.getPlayer().getInventory().addItem(event.getInventory().getItem(11));
			event.getInventory().setItem(11, air);
		}
		if (event.getInventory().getItem(15) != null)
		{
			event.getPlayer().getInventory().addItem(event.getInventory().getItem(15));
			event.getInventory().setItem(15, air);
		}
		if (event.getInventory().getItem(16) != null)
		{
			event.getPlayer().getInventory().addItem(event.getInventory().getItem(16));
			event.getInventory().setItem(16, air);
		}
	}

	/**
	 * 
	 * @param player
	 * @param inventory
	 */
	private void playerLaunchSmelt(final HumanEntity player, Inventory inventory)
	{
		// define withdraw and deposit item
		ItemStack slot1 = inventory.getItem(10);
		ItemStack slot2 = inventory.getItem(11);
		ItemStack slot3 = inventory.getItem(15);
		ItemStack slot4 = inventory.getItem(16);
		if (slot1 == null)
			slot1 = air;
		if (slot2 == null)
			slot2 = air;
		if (slot3 == null)
			slot3 = air;
		if (slot4 == null)
			slot4 = air;

		if (testBasicError(slot1, slot2, slot3, slot4, player) == true)
			return;
		defineSmelteryResult(slot1, slot2, inventory);

		// delete smelt item and lava bucket
		inventory.setItem(10, null);
		inventory.setItem(11, null);
		player.getInventory().removeItem(new ItemStack(Material.LAVA_BUCKET, 1));
		player.getInventory().addItem(new ItemStack(Material.BUCKET, 1));

		player.sendMessage(smelterySuccess);
		return;
	}

	/**
	 * 
	 * @param slot1 deposit slot
	 * @param slot2 deposit slot
	 * @param inventory
	 */
	private void defineSmelteryResult(final ItemStack slot1, final ItemStack slot2, Inventory inventory)
	{
		if (goldList.contains(slot1.getType()))
			inventory.setItem(15, new ItemStack(Material.GOLD_INGOT, 1));
		else if (ironList.contains(slot1.getType()))
			inventory.setItem(15, new ItemStack(Material.IRON_INGOT, 1));
		else if (diamondList.contains(slot1.getType()))
			inventory.setItem(15, new ItemStack(Material.DIAMOND, 1));
		if (goldList.contains(slot2.getType()))
			inventory.setItem(16, new ItemStack(Material.GOLD_INGOT, 1));
		else if (ironList.contains(slot2.getType()))
			inventory.setItem(16, new ItemStack(Material.IRON_INGOT, 1));
		else if (diamondList.contains(slot2.getType()))
			inventory.setItem(16, new ItemStack(Material.DIAMOND, 1));
	}

	/**
	 * 
	 * @param slot1 deposit slot
	 * @param slot2 deposit slot
	 * @param slot3 withdraw slot
	 * @param slot4 withdraw slot
	 * @param player
	 * @return
	 */
	private boolean testBasicError(final ItemStack slot1, final ItemStack slot2, final ItemStack slot3,
			final ItemStack slot4, final HumanEntity player)
	{
		// if nothing to smelt
		if (slot1 == air && slot2 == air)
		{
			player.sendMessage(nothingError);
			return true;
		}

		// if there is a spade or a hoe
		if (insufficientList.contains(slot1.getType()) || insufficientList.contains(slot2.getType()))
		{
			player.sendMessage(insufficientError);
			return true;
		}

		// if there is invalid item
		if (!smeltList.contains(slot1.getType()) || !smeltList.contains(slot2.getType()))
		{
			player.sendMessage(invalidItemError);
			return true;
		}

		// if withdraw zone is not empty
		if (slot3 != air || slot4 != air)
		{
			player.sendMessage(withdrawFullError);
			return true;
		}

		// if player didn't have lava bucket
		if (!player.getInventory().contains(Material.LAVA_BUCKET))
		{
			player.sendMessage(lavaError);
			return true;
		}

		return false;
	}
}

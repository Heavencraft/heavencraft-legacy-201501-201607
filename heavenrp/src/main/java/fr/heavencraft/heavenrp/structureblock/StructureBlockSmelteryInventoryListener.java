package fr.heavencraft.heavenrp.structureblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class StructureBlockSmelteryInventoryListener implements Listener
{
	private final String INVENTORYTITLE = ChatColor.RED + "              Fonderie";

	// Smeltery messages
	private final String INVALIDPLACEMESSAGE = ChatColor.RED
			+ "Ceci est la zone de récuperation des ressources. Vous ne pouvez pas placer d'objets dans cette zone.";
	private final String LAVAERROR = ChatColor.RED + "Il vous faut un seau de lave pour faire fonctionner la fonderie.";
	private final String INVALIDITEMERROR = ChatColor.RED
			+ "Certains des objets que vous voulez faire fondre, ne sont pas valides. Veuillez les retirer.";
	private final String NOTHINGERROR = ChatColor.RED + "Il n'y a rien à faire fondre.";
	private final String SMELTERYSUCCESS = ChatColor.DARK_GREEN + "Vous avez bien fait fondre vos Objets !";
	private final String WITHDRAWFULLERROR = ChatColor.RED + "Vous devez liberer la zone de récuperation.";
	private final String INSUFFICIENTERROR = ChatColor.RED
			+ "Au moins un des Objets que vous avez déposé ne vous rapportera rien.";

	// define list and map
	private final ArrayList<Integer> slotTakeList = new ArrayList<Integer>();
	private final ArrayList<Integer> slotPutList = new ArrayList<Integer>();
	private final ArrayList<InventoryAction> placeAction = new ArrayList<InventoryAction>();
	private final Map<Material, ItemStack> smeltResult = new HashMap<Material, ItemStack>();
	private final ArrayList<Material> insufficientList = new ArrayList<Material>();

	// define basic item
	private final ItemStack air = new ItemStack(Material.AIR);

	private final ItemStack simpleIron = new ItemStack(Material.IRON_INGOT, 1);
	private final ItemStack simpleGold = new ItemStack(Material.GOLD_INGOT, 1);
	private final ItemStack simpleDiamond = new ItemStack(Material.DIAMOND, 1);

	private final ItemStack doubleIron = new ItemStack(Material.IRON_INGOT, 2);
	private final ItemStack doubleGold = new ItemStack(Material.GOLD_INGOT, 2);
	private final ItemStack doubleDiamond = new ItemStack(Material.DIAMOND, 2);

	private final ItemStack tripleIron = new ItemStack(Material.IRON_INGOT, 3);
	private final ItemStack tripleGold = new ItemStack(Material.GOLD_INGOT, 3);
	private final ItemStack tripleDiamond = new ItemStack(Material.DIAMOND, 3);

	public StructureBlockSmelteryInventoryListener(HeavenPlugin plugin)
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

		// fill smelt Result
		smeltResult.put(Material.BUCKET, simpleIron);
		smeltResult.put(Material.IRON_AXE, simpleIron);
		smeltResult.put(Material.IRON_PICKAXE, simpleIron);
		smeltResult.put(Material.IRON_SWORD, simpleIron);
		smeltResult.put(Material.IRON_CHESTPLATE, tripleIron);
		smeltResult.put(Material.IRON_LEGGINGS, doubleIron);
		smeltResult.put(Material.IRON_BOOTS, simpleIron);
		smeltResult.put(Material.IRON_HELMET, simpleIron);
		smeltResult.put(Material.IRON_BARDING, tripleIron);

		smeltResult.put(Material.GOLD_AXE, simpleGold);
		smeltResult.put(Material.GOLD_PICKAXE, simpleGold);
		smeltResult.put(Material.GOLD_SWORD, simpleGold);
		smeltResult.put(Material.GOLD_CHESTPLATE, tripleGold);
		smeltResult.put(Material.GOLD_LEGGINGS, doubleGold);
		smeltResult.put(Material.GOLD_BOOTS, simpleGold);
		smeltResult.put(Material.GOLD_HELMET, simpleGold);
		smeltResult.put(Material.GOLD_BARDING, tripleGold);

		smeltResult.put(Material.DIAMOND_AXE, simpleDiamond);
		smeltResult.put(Material.DIAMOND_PICKAXE, simpleDiamond);
		smeltResult.put(Material.DIAMOND_SWORD, simpleDiamond);
		smeltResult.put(Material.DIAMOND_CHESTPLATE, tripleDiamond);
		smeltResult.put(Material.DIAMOND_LEGGINGS, doubleDiamond);
		smeltResult.put(Material.DIAMOND_BOOTS, simpleDiamond);
		smeltResult.put(Material.DIAMOND_HELMET, simpleDiamond);
		smeltResult.put(Material.DIAMOND_BARDING, tripleDiamond);

		smeltResult.put(Material.AIR, air);

		// fill insufficient list
		insufficientList.add(Material.IRON_SPADE);
		insufficientList.add(Material.IRON_HOE);
		insufficientList.add(Material.GOLD_SPADE);
		insufficientList.add(Material.GOLD_HOE);
		insufficientList.add(Material.DIAMOND_SPADE);
		insufficientList.add(Material.DIAMOND_HOE);

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	private void onPlayerInteractWithInventory(InventoryClickEvent event)
	{
		if (!event.getInventory().getName().equals(INVENTORYTITLE))
			return;

		final InventoryAction action = event.getAction();
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
		if (slotTakeList.contains(event.getRawSlot()))
		{
			if (placeAction.contains(action))
			{
				event.setCancelled(true);
				ChatUtil.sendMessage(event.getWhoClicked(), INVALIDPLACEMESSAGE);
				return;
			}
			return;
		}
		if (event.getRawSlot() == 13)
		{
			event.setCancelled(true);
			try
			{
				playerLaunchSmelt(event.getWhoClicked(), event.getInventory());
			}
			catch (HeavenException e)
			{
				ChatUtil.sendMessage(event.getWhoClicked(), e.getMessage());
				return;
			}
		}
		else
		{
			if (slotPutList.contains(event.getRawSlot()))
				return;
			event.setCancelled(true);
		}
	}

	@EventHandler
	private void onPlayerCloseInventory(InventoryCloseEvent event)
	{
		if (!event.getInventory().getName().equals(INVENTORYTITLE))
			return;

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
	 * When player want to smelt
	 * 
	 * @param player
	 * @param inventory
	 * @throws HeavenException
	 */
	private void playerLaunchSmelt(HumanEntity player, Inventory inventory) throws HeavenException
	{
		ItemStack slot1 = inventory.getItem(10);
		ItemStack slot2 = inventory.getItem(11);
		if (slot1 == null)
			slot1 = air;
		if (slot2 == null)
			slot2 = air;

		checkBasicError(player, slot1, slot2);

		inventory.setItem(15, smeltResult.get(slot1.getType()));
		inventory.setItem(16, smeltResult.get(slot2.getType()));

		inventory.setItem(10, null);
		inventory.setItem(11, null);
		player.getInventory().removeItem(new ItemStack(Material.LAVA_BUCKET, 1));
		player.getInventory().addItem(new ItemStack(Material.BUCKET, 1));

		player.sendMessage(SMELTERYSUCCESS);
	}

	/**
	 * check basic smeltery error
	 * 
	 * @param player
	 * @param slot1
	 * @param slot2
	 * @return
	 * @throws HeavenException
	 */
	private void checkBasicError(HumanEntity player, ItemStack slot1, ItemStack slot2) throws HeavenException
	{
		ItemStack slot3 = player.getOpenInventory().getTopInventory().getItem(15);
		ItemStack slot4 = player.getOpenInventory().getTopInventory().getItem(16);
		if (slot3 == null)
			slot3 = air;
		if (slot4 == null)
			slot4 = air;

		if ((slot1 == air) && (slot2 == air))
			throw new HeavenException(NOTHINGERROR);

		if ((insufficientList.contains(slot1.getType())) || (insufficientList.contains(slot2.getType())))
			throw new HeavenException(INSUFFICIENTERROR);

		if ((!smeltResult.containsKey(slot1.getType())) || (!smeltResult.containsKey(slot2.getType())))
			throw new HeavenException(INVALIDITEMERROR);

		if ((slot3 != air) || (slot4 != air))
			throw new HeavenException(WITHDRAWFULLERROR);

		if (!player.getInventory().contains(Material.LAVA_BUCKET))
			throw new HeavenException(LAVAERROR);

		return;
	}
}

package fr.heavencraft.heavencore.utils.menu;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.heavencraft.heavencore.CorePlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;

/**
 * API to create interactive menus.
 * @author Manuel. Based on different online examples.
 *
 */
public class MenuAPI {
	/*
	 *MenuAPI
	 */
	private static Map<String, Menu> menues = new HashMap<String, Menu>();
	private static Map<String, Inventory> sessions = new HashMap<String, Inventory>();
	private static Map<String, TextField> textFields = new HashMap<String, TextField>();
	private static Map<String, ItemStack> handSave = new HashMap<String, ItemStack>();
	
	/**
	 * Opens a menu
	 * @param player
	 * @param menu
	 * @throws HeavenException
	 */
	public static void openMenu(Player player, final Menu menu) throws HeavenException {
		if(player == null)
			throw new HeavenException("Cet utilisateur n'existe pas.");
		if(menu == null)
			throw new HeavenException("Ce menu n'existe pas.");
		
		if (MenuAPI.hasTextField(player)) {
			MenuAPI.closeTextField(player);
		}
		
		//Removed because resets pointer
		//MenuAPI.closeMenu(player);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(CorePlugin.getInstance(), new Runnable() {
			@Override
			public void run() {
				try
				{
					menu.show(player);
				}
				catch (HeavenException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public static boolean closeMenu(Player player) throws HeavenException {
		if(player == null)
			throw new HeavenException("Cet utilisateur n'existe pas.");
		
		if (!MenuAPI.hasMenu(player)) {
			return false;
		}
		MenuAPI.getMenues().remove(player.getName());
		MenuAPI.getSessions().remove(player.getName());
		player.closeInventory();
		return true;
	}
	
	/**
	 * Returns the opened menu of the player.
	 * @param player
	 * @return
	 */
	public static Menu getMenu(Player player) {
		return MenuAPI.getMenues().get(player.getName());
	}
	
	public static boolean hasMenu(Player player) {
		return MenuAPI.getMenu(player) != null;
	}
		
	public static boolean openTextField(Player player, TextField textField) throws HeavenException {
		if (MenuAPI.hasMenu(player)) {
			MenuAPI.closeMenu(player);
		}
		
		PlayerInventory pi = player.getInventory();
		int slot = 0;
		ItemStack item = null;
		while (slot < 9) {
			item = pi.getItem(slot);
			
			if (item == null || item.getType() == Material.AIR) {
				break;
			}
			slot++;
		}
		if (slot == 9) {
			slot = 0;
		}
		MenuAPI.closeMenu(player);
		
		item = pi.getItem(slot);
		if (item != null) {
			MenuAPI.handSave.put(player.getName(), item);
		}
		
		pi.setHeldItemSlot(slot);
		pi.setItem(slot, textField.toItemStack());
		player.updateInventory();
		
		if (textField.hasStartMessage()) {
			player.sendMessage(textField.getStartMessage());
		}
		
		MenuAPI.textFields.put(player.getName(), textField);
		return true;
	}
	
	public static boolean closeTextField(Player player) {
		if (!MenuAPI.hasTextField(player)) {
			return false;
		}
		
		ItemStack item = MenuAPI.handSave.get(player.getName());
		player.setItemInHand(item);
		
		player.updateInventory();
		
		MenuAPI.textFields.remove(player.getName());
		MenuAPI.handSave.remove(player.getName());
		return true;
	}
	
	public static TextField getTextField(Player player) {
		return MenuAPI.textFields.get(player.getName());
	}
	
	public static boolean hasTextField(Player player) {
		return (MenuAPI.getTextField(player) != null);
	}

	public static Map<String, Inventory> getSessions() {
		return sessions;
	}

	public static void setSessions(Map<String, Inventory> sessions) {
		MenuAPI.sessions = sessions;
	}

	public static Map<String, Menu> getMenues() {
		return menues;
	}

	public static void setMenues(Map<String, Menu> menues) {
		MenuAPI.menues = menues;
	}
	
}

package fr.heavencraft.heavencore.utils.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.CorePlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.options.Option;

public class MenuListener implements Listener {
	public MenuListener()
	{
		Bukkit.getPluginManager().registerEvents(this, CorePlugin.getInstance());
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onClick(InventoryClickEvent event) throws HeavenException {
		HumanEntity he = event.getWhoClicked();
		if (he instanceof Player) {
			Player player = (Player) he;
			
			if (!MenuAPI.hasMenu(player)) {
				return;
			}
			
			ClickType type = null;
			boolean left = event.isLeftClick();
			boolean shift = event.isShiftClick();
			if (left && shift) {
				type = ClickType.SHIFT_LEFT;
			} else if (left && !shift) {
				type = ClickType.LEFT;
			} else if (!left && shift) {
				type = ClickType.SHIFT_RIGHT;
			} else if (!left && !shift) {
				type = ClickType.RIGHT;
			} else {
				type = ClickType.UNKNOWN;
			}
			
			ItemStack cursor = event.getCursor();
			ItemStack current = event.getCurrentItem();
			
			Menu menu = MenuAPI.getMenu(player);
			
			int slot = event.getSlot();
			int slot_raw = event.getRawSlot();
			if (slot != slot_raw || slot >= menu.getSlots()) {
				// Inv slots are allowed
				if (event.isShiftClick()) {
					this.cancel(event);
					// But shift clicks are not
				}
				if (menu.hasClickHandler()) {
					if (menu.getClickHandler().onClick(player, cursor, current, type)) {
						this.cancel(event);
					}
				}
				return;
			}
			
			this.cancel(event);
			Option option = menu.getOption(slot_raw);
			
			if (option == null) {
				return;
			}
			
			option.performClick(menu, player, cursor, current, type);					
		}
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent event) throws HeavenException {
		HumanEntity he = event.getPlayer();
		if (he instanceof Player) {
			Player player = (Player) he;
			if (MenuAPI.hasMenu(player)) {
				Menu menu = MenuAPI.getMenu(player);
				MenuAPI.closeMenu(player);
				if (!menu.isCloseable()) {
					MenuAPI.openMenu(player, menu);
				}
			}
		}
	}
	
	public void cancel(InventoryClickEvent event) {
		event.setCancelled(true);
		((Player) event.getWhoClicked()).updateInventory();
	}
}

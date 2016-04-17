package fr.heavencraft.heavencore.utils.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.heavencraft.heavencore.CorePlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class TextFieldListener implements Listener {
	public TextFieldListener()
	{
		Bukkit.getPluginManager().registerEvents(this, CorePlugin.getInstance());
	}
	
	public void handleSign(final Player player) {
		if (MenuAPI.hasTextField(player)) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(CorePlugin.getInstance(), new Runnable() {
				@Override
				public void run() {
					ItemStack item = player.getItemInHand();
					if (item != null) {
						BookMeta meta = (BookMeta) item.getItemMeta();
						if (meta.hasPages() && meta.getPageCount() >= 1 && !meta.getPage(1).equals("")) {
							TextField textField = MenuAPI.getTextField(player);
							textField.onSend(player, meta.hasTitle() ? meta.getTitle() : "", meta.hasPages() ? meta.getPages().toArray(new String[meta.getPageCount()]) : new String[] {""});
							MenuAPI.closeTextField(player);
						} else if (item.getType() == Material.WRITTEN_BOOK) {
							TextField textField = MenuAPI.getTextField(player);
							MenuAPI.closeTextField(player);
							try
							{
								MenuAPI.openTextField(player, textField);
							}
							catch (HeavenException e)
							{
								e.printStackTrace();
							}
						}
					}
				}
			});
		}
	}
	
	@EventHandler
	public void onSign(PlayerEditBookEvent event) {
		this.handleSign(event.getPlayer());	
	}

	@EventHandler
	public void onScroll(PlayerItemHeldEvent event) {
		if (event.getPlayer() != null)
			if (MenuAPI.hasTextField(event.getPlayer())) 
				event.setCancelled(true);
			
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (MenuAPI.hasTextField(player)) {
			event.getItemDrop().remove();
			this.handleSign(player);
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		HumanEntity he = event.getWhoClicked();
		if (he instanceof Player) {
			Player player = (Player) he;
			if (MenuAPI.hasTextField(player)) {
				event.setCancelled(true);
				player.updateInventory();
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.handleQuit(event.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent event) {
		this.handleQuit(event.getPlayer());
	}
	
	private void handleQuit(Player player) {
		if (MenuAPI.hasTextField(player)) {
			MenuAPI.closeTextField(player);
		}
	}
}

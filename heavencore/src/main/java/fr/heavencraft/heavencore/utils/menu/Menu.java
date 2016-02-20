package fr.heavencraft.heavencore.utils.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.options.Option;

public class Menu implements Cloneable {
	
	public static final int MAX_HEIGHT = 6;
		
	private String title;
	
	private Option[][] options;
	
	private int width = 9;
	private int height;
	
	private boolean closeable = true;
	
	private ClickHandler clickHandler;
	
	public Menu(String title) {
		this(title, 3);
	}
	
	/**
	 * Constructor
	 * @param title Title of the menu
	 * @param lines Amount of lines for the menu
	 */
	public Menu(String title, int lines) {
		this.setTitle(title);
		if (lines > Menu.MAX_HEIGHT) {
			lines = Menu.MAX_HEIGHT; 
		}
		this.height = lines;
		this.options = new Option[9][lines];
	}
	
	public Menu setTitle(String title) {
		if (title.length() > 32) {
			title = title.substring(0, 32);
		}
		this.title = title;
		return this;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Adds a new item to the menu
	 * @param x X coordinate of the item
	 * @param y Y coordinate of the item
	 * @param option Item appearance, lore, etc..
	 * @return
	 */
	public Menu addOption(int x, int y, Option option) {
		this.options[x][y] = option;
		return this;
	}
	
	public Menu removeOption(int x, int y) {
		return this.addOption(x, y, null);
	}
	
	public Option getOption(int x, int y) {
		return this.options[x][y];
	}
	
	public Option getOption(int slot) {
		if (slot < 0 || slot > this.getSlots()) {
			return null;
		}
		return this.getOption(slot % 9, slot / 9);
	}
	
	public Option[][] getOptionGrid() {
		return this.options;
	}
	
	public List<Option> getOptions() {
		List<Option> options = new ArrayList<Option>();
		for (int x = 0; x < this.options.length; x++) {
			for (int y = 0; y < this.options[x].length; y++) {
				Option option = this.getOption(x, y);
				if (option != null) {
					options.add(option);
				}
			}
		}
		return options;
	}
	
	public final int getWidth() {
		return this.width;
	}
	
	/**
	 * Returns the number of lines.
	 * @return
	 */
	public final int getHeight() {
		return this.height;
	}
	
	/**
	 * Adds lines to a menu. Recommended for incrementing lines count only.
	 * @param lines
	 */
	public void extendLines(int lines){
		if (lines > Menu.MAX_HEIGHT) {
			lines = Menu.MAX_HEIGHT; 
		}
		this.height = lines;
		// Copy
		Option[][] oldOption = this.options;
		// Regenerate option table
		this.options = new Option[9][lines];
		// Add old elements
		for (int x = 0; x < oldOption.length; x++) {
			for (int y = 0; y < oldOption[x].length; y++) {
				Option option = oldOption[x][y];
				if (option != null) {
					this.options[x][y] = option;
				}
			}
		}
	}
	
	public final int getSlots() {
		return this.getWidth() * this.getHeight();
	}
	
	public Menu setCloseable(boolean closeable) {
		this.closeable = closeable;
		return this;
	}
	
	public boolean isCloseable() {
		return this.closeable;
	}
	
	public Menu setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
		return this;
	}
	
	public ClickHandler getClickHandler() {
		return this.clickHandler;
	}
	
	public boolean hasClickHandler() {
		return this.clickHandler != null;
	}
	
	/**
	 * Shows the inventory to the player
	 * Method should only be called through scheduled task. (openInventory during Inventory Click event)
	 * @param player
	 * @throws HeavenException
	 */
	public final void show(final Player player) throws HeavenException {
		Inventory inv = Bukkit.createInventory(null, this.getSlots(), this.getTitle());
		// Set content
		for (int x = 0; x < this.options.length; x++) {
			for (int y = 0; y < this.options[x].length; y++) {
				Option option = this.getOption(x, y);
				ItemStack item = null;
				if (option != null) {
					item = option.toItemStack();
				}
				inv.setItem(x + y * 9, item);
			}
		}
		player.openInventory(inv);
		// Update session lists
		MenuAPI.getSessions().put(player.getName(), inv);
		MenuAPI.getMenues().put(player.getName(), this);
	}
	
	@Deprecated
	public final void refresh(Player player) throws HeavenException {
		Inventory inv = null;
		String name = player.getName();
		// does the player
		if (MenuAPI.getSessions().containsKey(name)) {
			inv = MenuAPI.getSessions().get(name);
		}
		boolean newName = inv == null ? false : !inv.getTitle().equals(this.getTitle());
		if (inv == null || newName) {
			if (newName) {
				MenuAPI.openMenu(player, this);
				return;
			}
			inv = Bukkit.createInventory(null, this.getSlots(), this.getTitle());
		}
		for (int x = 0; x < this.options.length; x++) {
			for (int y = 0; y < this.options[x].length; y++) {
				Option option = this.getOption(x, y);
				ItemStack item = null;
				if (option != null) {
					item = option.toItemStack();
				}
				inv.setItem(x + y * 9, item);
			}
		}
		if (inv != MenuAPI.getSessions().get(name)) {
			player.openInventory(inv);
		} else {
			player.updateInventory();
		}
		MenuAPI.getSessions().put(name, inv);
		MenuAPI.getMenues().put(name, this);
	}
	
	@Override
	public Menu clone() {
		return this.clone(this.getTitle());
	}
	
	public Menu clone(String title) {
		Menu menu = new Menu(title, this.getHeight());
		for (int x = 0; x < this.options.length; x++) {
			for (int y = 0; y < this.options[x].length; y++) {
				menu.addOption(x, y, this.getOption(x, y));
			}
		}
		menu.setCloseable(this.isCloseable());
		if (this.hasClickHandler()) {
			menu.setClickHandler(this.getClickHandler());
		}
		return menu;
	}
}

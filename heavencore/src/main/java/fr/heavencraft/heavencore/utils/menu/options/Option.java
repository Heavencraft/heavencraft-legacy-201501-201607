package fr.heavencraft.heavencore.utils.menu.options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;


public abstract class Option {
	
	private Material type;
	private short damage = (short) 0;
	private int amount = 1;

	private String name = "Default name - please change";
	private String[] description;
	
	private String clickMessage = null;
	private String permission = null;
	private String permissionMessage = null;
	
	/**
	 * Constructor: Simple menu item
	 * @param type Material type
	 */
	public Option(Material type) {
		this.setType(type);
	}
	
	/**
	 * Constructor: Menu item
	 * @param type Material Type
	 * @param name Custom Name of the item
	 * @param description Lore
	 */
	public Option(Material type, String name, String... description) {
		this(type);
		this.setName(name);
		this.setDescription(description);
	}
	
	public Option(Material type, short damage, String name, String... description) {
		this(type, name, description);
		this.setDamage(damage);
	}
	
	public Option(Material type, short damage, int amount, String name, String... description) {
		this(type, damage, name, description);
		this.setAmount(amount);
	}
	
	public Option setType(Material type) {
		this.type = type;
		return this;
	}
	
	public Material getType() {
		return this.type;
	}
	
	public Option setDamage(short damage) {
		this.damage = damage;
		return this;
	}
	
	public short getDamage() {
		return this.damage;
	}
	
	public Option setAmount(int amount) {
		this.amount = amount;
		return this;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public Option setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Option setDescription(List<String> desc) {
		this.setDescription(desc.toArray(new String[desc.size()]));
		return this;
	}
	
	public Option setDescription(String... desc) {
		this.description = desc;
		return this;
	}
	
	public String[] getDescription() {
		return this.description;
	}
	
	public boolean hasDescription() {
		return this.getDescription() != null;
	}
	
	public Option setClickMessage(String message) {
		this.clickMessage = message;
		return this;
	}
	
	public String getClickMessage() {
		return this.clickMessage;
	}
	
	public boolean hasClickMessage() {
		return this.getClickMessage() != null;
	}
	
	public Option setPermission(String permission) {
		this.permission = permission;
		return this;
	}
	
	public String getPermission() {
		return this.permission;
	}
	
	public boolean hasPermission() {
		return this.getPermission() != null;
	}
	
	public Option setPermissionMessage(String message) {
		this.permissionMessage = message;
		return this;
	}
	
	public String getPermissionMessage() {
		return this.permissionMessage;
	}
	
	public boolean hasPermissionMessage() {
		return this.getPermissionMessage() != null;
	}
	
	public ItemStack toItemStack() {
		ItemStack item = new ItemStack(this.getType(), this.getAmount(), this.getDamage());
		ItemMeta meta = Bukkit.getItemFactory().getItemMeta(item.getType());
		meta.setDisplayName(this.getName());
		List<String> lore = new ArrayList<String>();
		if (this.hasDescription()) {
			for (String line : this.getDescription()) {
				lore.add(line);
			}
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public final void performClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException {
		if (!this.hasPermission() || player.hasPermission(this.getPermission())) {
			if (this.hasClickMessage()) {
				player.sendMessage(this.getClickMessage());
			}
			this.onClick(menu, player, cursor, current, type);
		} else {
			if (this.hasPermissionMessage()) {
				player.sendMessage(this.getPermissionMessage());
			}
		}
	}
	
	public abstract void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException;
}

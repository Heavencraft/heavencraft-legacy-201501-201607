package fr.heavencraft.heavenrp.dungeon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class DungeonPlayerInventoryStore
{
	private final static Map<UUID, ItemStack[]> inventoryStore = new HashMap<UUID, ItemStack[]>();
	private final static Map<UUID, ItemStack[]> armorStore = new HashMap<UUID, ItemStack[]>();
	
	/**
	 * Stores a players inventory and armor.
	 * @param p
	 */
	public static void StoreInventory(Player p) {
		// Store
		inventoryStore.put(p.getUniqueId(), p.getInventory().getContents());
		armorStore.put(p.getUniqueId(), p.getInventory().getArmorContents());
		// Clear inventory
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
	}
	
	/**
	 * Restores a players inventory and armor.
	 * @param p
	 * @throws HeavenException 
	 */
	public static void RestoreInventory(Player p) throws HeavenException {
		if(!inventoryStore.containsKey(p.getUniqueId()))
			throw new HeavenException("Impossible de restore l'inventaire. Le joueur %1$s n'a pas de données stockées.",  p.getName());
		// Restore
		p.getInventory().setContents(inventoryStore.get(p.getUniqueId()));
		p.getInventory().setArmorContents(armorStore.get(p.getUniqueId()));
		// Remove from store
		inventoryStore.remove(p.getUniqueId());
		armorStore.remove(p.getUniqueId());
	}
}

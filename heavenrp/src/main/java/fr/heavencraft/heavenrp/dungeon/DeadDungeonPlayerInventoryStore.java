package fr.heavencraft.heavenrp.dungeon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DeadDungeonPlayerInventoryStore
{
	private final static Map<UUID, ItemStack[]> inventoryStore = new HashMap<UUID, ItemStack[]>();
	private final static Map<UUID, ItemStack[]> armorStore = new HashMap<UUID, ItemStack[]>();

	/**
	 * Stores a players inventory and armor.
	 * 
	 * @param p
	 */
	public static void StoreInventory(Player p)
	{
		// Store
		inventoryStore.put(p.getUniqueId(), p.getInventory().getContents());
		armorStore.put(p.getUniqueId(), p.getInventory().getArmorContents());
		// Clear inventory
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
	}

	/**
	 * Restores a players inventory and armor.
	 * 
	 * @param p
	 */
	public static void RestoreInventory(Player p)
	{
		if (!inventoryStore.containsKey(p.getUniqueId()))
			return;
		// throw new HeavenException("Impossible de restore l'inventaire. Le
		// joueur %1$s n'a pas de données stockées.", p.getName());
		// Restore
		p.getInventory().setContents(inventoryStore.get(p.getUniqueId()));
		p.getInventory().setArmorContents(armorStore.get(p.getUniqueId()));
		// Remove from store
		inventoryStore.remove(p.getUniqueId());
		armorStore.remove(p.getUniqueId());
	}
	
	/**
	 * Simply removes a stored inventory
	 * @param uid Players UUID
	 */
	public static void DiscardInventory(UUID uid) {
		if(!inventoryStore.containsKey(uid))
			return;
		// Remove from store
		inventoryStore.remove(uid);
		armorStore.remove(uid);
	}

	/**
	 * Returns if a player is dead and has it's inventory stored
	 * 
	 * @param p
	 * @return
	 */
	public static boolean hasStored(Player p)
	{
		return inventoryStore.containsKey(p.getUniqueId());
	}
}

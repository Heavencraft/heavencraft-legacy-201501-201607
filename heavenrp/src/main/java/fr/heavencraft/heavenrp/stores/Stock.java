package fr.heavencraft.heavenrp.stores;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavenrp.utils.RPUtils;

public class Stock
{
	private String _ownerName;
	private String _storeName;
	private String _chestPosition;
	private Sign _linkedSign;

	private boolean _isValid;

	public Stock(String ownerName, String storeName, Chest chest, Sign linkedSign)
	{
		_ownerName = ownerName;
		_storeName = storeName;
		_chestPosition = RPUtils.blockToString(chest.getBlock());
		_linkedSign = linkedSign;

		_isValid = true;
	}

	public Stock(String line)
	{
		_isValid = false;

		final String[] lineData = line.split(";");

		if (lineData.length != 4)
			return;

		_ownerName = lineData[0];
		_storeName = lineData[1];

		final Block chestBlock = RPUtils.stringToBlock(lineData[2]);

		if (chestBlock == null || !(chestBlock.getState() instanceof Chest))
			return;

		_chestPosition = lineData[2];

		final Block signCraftBlock = RPUtils.stringToBlock(lineData[3]);

		if (signCraftBlock == null || !(signCraftBlock.getState() instanceof Sign))
			return;

		_linkedSign = (Sign) signCraftBlock.getState();
		_isValid = true;
	}

	public String getSaveString()
	{
		return _ownerName + ";" + _storeName + ";" + _chestPosition + ";"
				+ RPUtils.blockToString(_linkedSign.getBlock());
	}

	public boolean isValid()
	{
		return _isValid;
	}

	public String getOwnerName()
	{
		return _ownerName;
	}

	public String getStoreName()
	{
		return _storeName;
	}

	public Chest getChest()
	{
		return (Chest) RPUtils.stringToBlock(_chestPosition).getState();
	}

	public Sign getLinkedSign()
	{
		return _linkedSign;
	}

	private static int getItemQuantity(ItemStack items[], Material itemId, int itemData)
	{
		int quantity = 0;

		for (final ItemStack item : items)
		{
			if (item != null && item.getType() == itemId
					&& (itemData == -1 || ((item.getData() != null) && item.getDurability() == itemData)))
			{
				quantity += item.getAmount();
			}
		}
		return quantity;
	}

	public int getItemQuantity(Material itemId, int itemData)
	{
		return getItemQuantity(getChest().getInventory().getContents(), itemId, itemData);
	}

	public static int getItemQuantity(Player player, Store store)
	{
		return getItemQuantity(player.getInventory().getContents(), store.getMaterial(), store.getMaterialData());
	}

	private static boolean removeStack(Inventory inventory, ItemStack stack)
	{
		int left = stack.getAmount();
		final ItemStack[] contents = inventory.getContents();

		for (int i = 0; (i < contents.length) && (left != 0); i++)
		{
			final ItemStack s = contents[i];

			if (s == null)
				continue;

			int data1 = -1;
			int data2 = -1;
			if (s.getData() != null)
				data1 = s.getDurability();
			if (stack.getData() != null)
				data2 = stack.getDurability();
			if ((s.getType() != stack.getType()) || (data1 != data2))
			{
				continue;
			}

			final int size = s.getAmount();
			final int newSize = size - Math.min(size, left);

			if (newSize == 0)
			{
				inventory.setItem(i, null);
			}
			else
			{
				s.setAmount(newSize);
				inventory.setItem(i, s);
			}

			left -= size - newSize;
		}

		return left == 0;
	}

	public boolean removeStack(ItemStack stack)
	{
		return removeStack(getChest().getInventory(), stack);
	}

	public static boolean removeStack(Player player, ItemStack stack)
	{
		return removeStack(player.getInventory(), stack);
	}
}
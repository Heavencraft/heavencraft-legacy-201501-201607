package fr.heavencraft.heavenrp.jobs;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BlockType
{
	private final Material type;
	private final byte data;

	public BlockType(Material type, byte data)
	{
		this.type = type;
		this.data = data;
	}

	public BlockType(ItemStack item)
	{
		this.type = item.getType();
		this.data = item.getData().getData();
	}

	public BlockType(Block block)
	{
		this.type = block.getType();
		this.data = block.getData();
	}

	@Override
	public String toString()
	{
		return type + ":" + data;
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + (type == null ? 0 : type.hashCode());
		result = 31 * result + data;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BlockType other = (BlockType) obj;
		if (data != other.data)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
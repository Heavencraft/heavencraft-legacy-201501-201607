package fr.heavencraft.heavenrp.jobs;

import org.bukkit.Material;

public class BlockType
{
	private final Material type;
	private final byte data;

	public BlockType(Material type, byte data)
	{
		this.type = type;
		this.data = data;
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
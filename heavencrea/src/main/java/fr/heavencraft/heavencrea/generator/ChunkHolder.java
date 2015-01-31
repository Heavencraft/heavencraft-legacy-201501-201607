package fr.heavencraft.heavencrea.generator;

import org.bukkit.Material;

class ChunkHolder
{
	private final short[][] result = new short[16][];

	@SuppressWarnings("deprecation")
	public void setBlock(int x, int y, int z, Material type)
	{
		if (result[y >> 4] == null)
		{
			result[y >> 4] = new short[4096];
		}

		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (short) type.getId();
	}

	public short[][] getResult()
	{
		return result;
	}
}
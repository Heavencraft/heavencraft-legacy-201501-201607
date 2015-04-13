package fr.heavencraft.heavencrea.generator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class FlatChunkGenerator extends ChunkGenerator
{
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		return generateGround(biomes).getResult();
	}

	/*
	 * Ground
	 */

	protected static ChunkHolder generateGround(BiomeGrid biomes)
	{
		final ChunkHolder chunk = new ChunkHolder();

		for (int x = 0; x != 16; x++)
		{
			for (int z = 0; z != 16; z++)
			{
				// O -> 4 : BEDROCK
				for (int y = 0; y != 5; y++)
					chunk.setBlock(x, y, z, Material.BEDROCK);

				// 5 -> 45 : STONE
				for (int y = 5; y != 46; y++)
					chunk.setBlock(x, y, z, Material.STONE);

				// 46 -> 49 : DIRT
				for (int y = 46; y != 50; y++)
					chunk.setBlock(x, y, z, Material.DIRT);

				// 50 : GRASS
				chunk.setBlock(x, 50, z, Material.GRASS);

				// Biome
				biomes.setBiome(x, z, Biome.PLAINS);
			}
		}

		return chunk;
	}
}
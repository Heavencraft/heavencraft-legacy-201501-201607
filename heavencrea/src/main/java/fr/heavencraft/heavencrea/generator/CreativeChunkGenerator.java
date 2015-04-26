package fr.heavencraft.heavencrea.generator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

public class CreativeChunkGenerator extends AbstractPlotChunkGenerator
{
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		final ChunkHolder chunk = generateGround(biomes);

		if (x == 0 || z == 0)
			generateLargeRoad(chunk, x, z, Material.SANDSTONE);
		if (x < 0 && z > 0)
		{
			generatePlot(chunk, x, z, SMALL_PLOT, Material.EMERALD_BLOCK);
			generateSmallRoad(chunk, x, z, SMALL_PLOT, 2, Material.SANDSTONE);
		}
		else if (x > 0 && z > 0)
		{
			generatePlot(chunk, x, z, MEDIUM_PLOT, Material.EMERALD_BLOCK);
			generateSmallRoad(chunk, x, z, MEDIUM_PLOT, 1, Material.SANDSTONE);
		}
		else if (x > 0 && z < 0)
		{
			generatePlot(chunk, x, z, LARGE_PLOT, Material.EMERALD_BLOCK);
			generateSmallRoad(chunk, x, z, LARGE_PLOT, 1, Material.SANDSTONE);
		}

		return chunk.getResult();
	}
}
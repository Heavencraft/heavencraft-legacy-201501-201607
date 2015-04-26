package fr.heavencraft.heavencrea.generator;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;

public class TalentChunkGenerator extends AbstractPlotChunkGenerator
{
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		final ChunkHolder chunk = generateGround(biomes);

		generatePlot(chunk, x, z, MEDIUM_PLOT, Material.GOLD_BLOCK);
		generateSmallRoad(chunk, x, z, MEDIUM_PLOT, 1, Material.CLAY);

		return chunk.getResult();
	}
}
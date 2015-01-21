package fr.heavencraft.heavencrea;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class CreativeChunkGenerator extends ChunkGenerator
{
	private static final int SMALL_PLOT = 2;
	private static final int MEDIUM_PLOT = 5;
	private static final int LARGE_PLOT = 11;

	private static final int SMALL_ROAD = 2;
	private static final int LARGE_ROAD = 4;

	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		final ChunkHolder chunk = generateGround(biomes);

		if (x == 0 || z == 0)
			generateLargeRoad(chunk, x, z);
		if (x < 0 && z > 0)
		{
			generatePlot(chunk, x, z, SMALL_PLOT);
			generateSmallRoad(chunk, x, z, SMALL_PLOT, 2);
		}
		else if (x > 0 && z > 0)
		{
			generatePlot(chunk, x, z, MEDIUM_PLOT);
			generateSmallRoad(chunk, x, z, MEDIUM_PLOT, 1);
		}
		else if (x > 0 && z < 0)
		{
			generatePlot(chunk, x, z, LARGE_PLOT);
			generateSmallRoad(chunk, x, z, LARGE_PLOT, 1);
		}

		return chunk.getResult();
	}

	private static void generatePlot(ChunkHolder chunk, int chunkX, int chunkZ, int plotSize)
	{
		int modX = chunkX % (plotSize + 1);
		int modZ = chunkZ % (plotSize + 1);

		if (modX < 0)
			modX = plotSize + modX + 1;
		if (modZ < 0)
			modZ = plotSize + modZ + 1;

		if (modX == 0 || modZ == 0)
			return; // Do nothing : space between two plots

		if (modX != 1 && modX != plotSize && modZ != 1 && modZ != plotSize)
			return; // Do nothing : space into the plot

		if (modX == 1) // West border
			for (int z = 0; z != 16; z++)
				chunk.setBlock(0, 51, z, Material.EMERALD_BLOCK);

		if (modX == plotSize) // East border
			for (int z = 0; z != 16; z++)
				chunk.setBlock(15, 51, z, Material.EMERALD_BLOCK);

		if (modZ == 1) // South border
			for (int x = 0; x != 16; x++)
				chunk.setBlock(x, 51, 0, Material.EMERALD_BLOCK);

		if (modZ == plotSize) // North border
			for (int x = 0; x != 16; x++)
				chunk.setBlock(x, 51, 15, Material.EMERALD_BLOCK);
	}

	/*
	 * Roads
	 */

	private static void generateLargeRoad(ChunkHolder chunk, int chunkX, int chunkZ)
	{
		if (chunkX == 0 && chunkZ == 0) // Center
			generateCrossRoad(chunk, LARGE_ROAD, LARGE_ROAD);
		else if (chunkX == 0) // North South
		{
			if (chunkZ % 6 == 0)
				generateCrossRoad(chunk, LARGE_ROAD, SMALL_ROAD);
			else
				generateNorthSouthRoad(chunk, LARGE_ROAD);
		}
		else if (chunkZ == 0) // East West
		{
			if (chunkX % 6 == 0)
				generateCrossRoad(chunk, SMALL_ROAD, LARGE_ROAD);
			else
				generateEastWestRoad(chunk, LARGE_ROAD);
		}
	}

	private static void generateSmallRoad(ChunkHolder chunk, int chunkX, int chunkZ, int plotSize, int nbPlotsBetweenRoads)
	{
		final int modX = chunkX % (nbPlotsBetweenRoads * (plotSize + 1));
		final int modZ = chunkZ % (nbPlotsBetweenRoads * (plotSize + 1));

		if (modX == 0 && modZ == 0)
			generateCrossRoad(chunk, SMALL_ROAD, SMALL_ROAD);
		else if (modX == 0)
			generateNorthSouthRoad(chunk, SMALL_ROAD);
		else if (modZ == 0)
			generateEastWestRoad(chunk, SMALL_ROAD);
	}

	private static void generateNorthSouthRoad(ChunkHolder chunk, int width)
	{
		final int startX = 8 - width;
		final int stopX = 8 + width;

		for (int z = 0; z != 16; z++)
		{
			for (int x = startX; x != stopX; x++)
				chunk.setBlock(x, 50, z, Material.SANDSTONE);

			chunk.setBlock(startX - 1, 51, z, Material.STEP);
			chunk.setBlock(stopX, 51, z, Material.STEP);
		}
	}

	private static void generateEastWestRoad(ChunkHolder chunk, int width)
	{
		final int startZ = 8 - width;
		final int stopZ = 8 + width;

		for (int x = 0; x != 16; x++)
		{
			for (int z = startZ; z != stopZ; z++)
				chunk.setBlock(x, 50, z, Material.SANDSTONE);

			chunk.setBlock(x, 51, startZ - 1, Material.STEP);
			chunk.setBlock(x, 51, stopZ, Material.STEP);
		}
	}

	private static void generateCrossRoad(ChunkHolder chunk, int widthNS, int widthEW)
	{
		generateNorthSouthRoad(chunk, widthNS);
		generateEastWestRoad(chunk, widthEW);

		final int startX = 8 - widthNS;
		final int stopX = 8 + widthNS;

		final int startZ = 8 - widthEW;
		final int stopZ = 8 + widthEW;

		for (int x = startX; x != stopX; x++)
		{
			chunk.setBlock(x, 51, startZ - 1, Material.AIR);
			chunk.setBlock(x, 51, stopZ, Material.AIR);
		}

		for (int z = startZ; z != stopZ; z++)
		{
			chunk.setBlock(startX - 1, 51, z, Material.AIR);
			chunk.setBlock(stopX, 51, z, Material.AIR);
		}
	}

	/*
	 * Ground
	 */

	private static ChunkHolder generateGround(BiomeGrid biomes)
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
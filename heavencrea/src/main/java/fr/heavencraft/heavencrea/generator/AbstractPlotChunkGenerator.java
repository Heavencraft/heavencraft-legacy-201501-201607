package fr.heavencraft.heavencrea.generator;

import org.bukkit.Material;

public abstract class AbstractPlotChunkGenerator extends FlatChunkGenerator
{
	public static final int SMALL_PLOT = 2;
	public static final int MEDIUM_PLOT = 5;
	public static final int LARGE_PLOT = 11;

	private static final int SMALL_ROAD = 2;
	private static final int LARGE_ROAD = 4;

	protected static void generatePlot(ChunkHolder chunk, int chunkX, int chunkZ, int plotSize, Material border)
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
				chunk.setBlock(0, 51, z, border);

		if (modX == plotSize) // East border
			for (int z = 0; z != 16; z++)
				chunk.setBlock(15, 51, z, border);

		if (modZ == 1) // South border
			for (int x = 0; x != 16; x++)
				chunk.setBlock(x, 51, 0, border);

		if (modZ == plotSize) // North border
			for (int x = 0; x != 16; x++)
				chunk.setBlock(x, 51, 15, border);
	}

	/*
	 * Roads
	 */

	protected static void generateLargeRoad(ChunkHolder chunk, int chunkX, int chunkZ, Material road)
	{
		if (chunkX == 0 && chunkZ == 0) // Center
			generateCrossRoad(chunk, LARGE_ROAD, LARGE_ROAD, road);
		else if (chunkX == 0) // North South
		{
			if (chunkZ % 6 == 0)
				generateCrossRoad(chunk, LARGE_ROAD, SMALL_ROAD, road);
			else
				generateNorthSouthRoad(chunk, LARGE_ROAD, road);
		}
		else if (chunkZ == 0) // East West
		{
			if (chunkX % 6 == 0)
				generateCrossRoad(chunk, SMALL_ROAD, LARGE_ROAD, road);
			else
				generateEastWestRoad(chunk, LARGE_ROAD, road);
		}
	}

	protected static void generateSmallRoad(ChunkHolder chunk, int chunkX, int chunkZ, int plotSize,
			int nbPlotsBetweenRoads, Material road)
	{
		final int modX = chunkX % (nbPlotsBetweenRoads * (plotSize + 1));
		final int modZ = chunkZ % (nbPlotsBetweenRoads * (plotSize + 1));

		if (modX == 0 && modZ == 0)
			generateCrossRoad(chunk, SMALL_ROAD, SMALL_ROAD, road);
		else if (modX == 0)
			generateNorthSouthRoad(chunk, SMALL_ROAD, road);
		else if (modZ == 0)
			generateEastWestRoad(chunk, SMALL_ROAD, road);
	}

	private static void generateNorthSouthRoad(ChunkHolder chunk, int width, Material road)
	{
		final int startX = 8 - width;
		final int stopX = 8 + width;

		for (int z = 0; z != 16; z++)
		{
			for (int x = startX; x != stopX; x++)
				chunk.setBlock(x, 50, z, road);

			chunk.setBlock(startX - 1, 51, z, Material.STEP);
			chunk.setBlock(stopX, 51, z, Material.STEP);
		}
	}

	private static void generateEastWestRoad(ChunkHolder chunk, int width, Material road)
	{
		final int startZ = 8 - width;
		final int stopZ = 8 + width;

		for (int x = 0; x != 16; x++)
		{
			for (int z = startZ; z != stopZ; z++)
				chunk.setBlock(x, 50, z, road);

			chunk.setBlock(x, 51, startZ - 1, Material.STEP);
			chunk.setBlock(x, 51, stopZ, Material.STEP);
		}
	}

	private static void generateCrossRoad(ChunkHolder chunk, int widthNS, int widthEW, Material road)
	{
		generateNorthSouthRoad(chunk, widthNS, road);
		generateEastWestRoad(chunk, widthEW, road);

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
}
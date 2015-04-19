package fr.heavencraft.heavencrea.plots;

import org.bukkit.Location;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;

abstract class AbstractPlotSignListener extends AbstractSignListener
{
	private static final int SMALL_PRICE = 500;
	private static final int MEDIUM_PRICE = 2500;
	private static final int LARGE_PRICE = 10000;

	public AbstractPlotSignListener(HeavenPlugin plugin, String tag, String permission)
	{
		super(plugin, tag, permission);
	}

	protected Plot getNormalPlotAt(Location location) throws HeavenException
	{
		if (!location.getWorld().getName().equals("world_creative"))
			throw new HeavenException("Ce panneau est invalide");

		final int x = location.getChunk().getX();
		final int z = location.getChunk().getZ();

		if (x < 0 && z > 0)
			return getPlot(x, z, CreativeChunkGenerator.SMALL_PLOT, SMALL_PRICE);
		else if (x > 0 && z > 0)
			return getPlot(x, z, CreativeChunkGenerator.MEDIUM_PLOT, MEDIUM_PRICE);
		else if (x > 0 && z < 0)
			return getPlot(x, z, CreativeChunkGenerator.LARGE_PLOT, LARGE_PRICE);
		else
			throw new HeavenException("Ce panneau est invalide");
	}

	protected Plot getTalentPlotAt(Location location) throws HeavenException
	{
		if (!location.getWorld().getName().equals("world_creative"))
			throw new HeavenException("Ce panneau est invalide");

		final int x = location.getChunk().getX();
		final int z = location.getChunk().getZ();

		if (x < 0 && z < 0)
			return getPlot(x, z, CreativeChunkGenerator.SMALL_PLOT, SMALL_PRICE);
		else
			throw new HeavenException("Ce panneau est invalide");
	}

	private Plot getPlot(int chunkX, int chunkZ, int plotSize, int price)
	{
		int plotX = chunkX / (plotSize + 1);
		int plotZ = chunkZ / (plotSize + 1);

		if (chunkX < 0)
			plotX -= 1;
		if (chunkZ < 0)
			plotZ -= 1;

		final int minX = plotX * 16 * (plotSize + 1) + 16;
		final int maxX = (plotX + 1) * 16 * (plotSize + 1) - 1;

		final int minZ = plotZ * 16 * (plotSize + 1) + 16;
		final int maxZ = (plotZ + 1) * 16 * (plotSize + 1) - 1;

		final Plot plot = new Plot();
		plot.price = price;

		if (minX < maxX)
		{
			plot.minX = minX;
			plot.maxX = maxX;
		}
		else
		{
			plot.maxX = minX;
			plot.minX = maxX;
		}

		if (minZ < maxZ)
		{
			plot.minZ = minZ;
			plot.maxZ = maxZ;
		}
		else
		{
			plot.maxZ = minZ;
			plot.minZ = maxZ;
		}

		return plot;
	}

	class Plot
	{
		int minX;
		int minZ;
		int maxX;
		int maxZ;
		int price;
	}
}
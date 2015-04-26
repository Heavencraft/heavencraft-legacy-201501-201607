package fr.heavencraft.heavencrea.plots;

import org.bukkit.Location;
import org.bukkit.World;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;
import fr.heavencraft.heavencrea.generator.TalentChunkGenerator;
import fr.heavencraft.heavencrea.worlds.WorldsManager;

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
		final World world = location.getWorld();

		if (!WorldsManager.getWorldCreative().equals(world))
			throw new HeavenException("Ce panneau est invalide");

		final int x = location.getChunk().getX();
		final int z = location.getChunk().getZ();

		if (x < 0 && z > 0)
			return getPlot(world, x, z, CreativeChunkGenerator.SMALL_PLOT, SMALL_PRICE);
		else if (x > 0 && z > 0)
			return getPlot(world, x, z, CreativeChunkGenerator.MEDIUM_PLOT, MEDIUM_PRICE);
		else if (x > 0 && z < 0)
			return getPlot(world, x, z, CreativeChunkGenerator.LARGE_PLOT, LARGE_PRICE);
		else
			throw new HeavenException("Ce panneau est invalide");
	}

	protected Plot getTalentPlotAt(Location location) throws HeavenException
	{
		final World world = location.getWorld();
		final int x = location.getChunk().getX();
		final int z = location.getChunk().getZ();

		if (WorldsManager.getWorldCreative().equals(world))
		{
			if (x < 0 && z < 0)
				return getPlot(world, x, z, CreativeChunkGenerator.SMALL_PLOT, 0);
			else
				throw new HeavenException("Ce panneau est invalide");
		}

		if (WorldsManager.getWorldTalent().equals(world))
		{
			return getPlot(world, x, z, TalentChunkGenerator.MEDIUM_PLOT, 0);
		}

		throw new HeavenException("Ce panneau est invalide");
	}

	private Plot getPlot(World world, int chunkX, int chunkZ, int plotSize, int price)
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
		plot.world = world;
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
		World world;
		int minX;
		int minZ;
		int maxX;
		int maxZ;
		int price;
	}
}
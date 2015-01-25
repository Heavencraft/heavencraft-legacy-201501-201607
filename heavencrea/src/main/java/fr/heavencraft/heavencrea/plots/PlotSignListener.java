package fr.heavencraft.heavencrea.plots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencrea.CreativeChunkGenerator;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlotSignListener extends AbstractSignListener
{
	private static final int SMALL_PRICE = 500;
	private static final int MEDIUM_PRICE = 2500;
	private static final int LARGE_PRICE = 10000;

	public PlotSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Parcelle", "");
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		final Plot plot = getPlotAt(sign.getLocation());

		final Region region = HeavenGuard.getInstance().getRegionProvider()
				.createRegion("parcelle_" + player.getName(), sign.getLocation().getWorld().getName(),//
						plot.minX, 0, plot.minZ, //
						plot.maxX, 0xFF, plot.maxZ);

		region.addMember(player.getUniqueId(), true);

		sign.getBlock().breakNaturally();

		// Set redstone border
		for (int x = plot.minX; x <= plot.maxX; x++)
		{
			Bukkit.getWorld("world_creative").getBlockAt(x, 51, plot.minZ).setType(Material.REDSTONE_BLOCK);
			Bukkit.getWorld("world_creative").getBlockAt(x, 51, plot.maxZ).setType(Material.REDSTONE_BLOCK);
		}
		for (int z = plot.minZ; z <= plot.maxZ; z++)
		{
			Bukkit.getWorld("world_creative").getBlockAt(plot.minX, 51, z).setType(Material.REDSTONE_BLOCK);
			Bukkit.getWorld("world_creative").getBlockAt(plot.maxX, 51, z).setType(Material.REDSTONE_BLOCK);
		}

		plugin.sendMessage(player, "Vous venez d'acheter la parcelle {%1$s} pour {%2$s} jetons.", region.getName(), plot.price);
	}

	@Override
	protected void onSignBreak(Player player, Sign sign) throws HeavenException
	{
	}

	private Plot getPlotAt(Location location) throws HeavenException
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

		if (plot.minX < plot.maxX)
		{
			plot.minX = minX;
			plot.maxX = maxX;
		}
		else
		{
			plot.maxX = minX;
			plot.minX = maxX;
		}

		if (plot.minZ < plot.maxZ)
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
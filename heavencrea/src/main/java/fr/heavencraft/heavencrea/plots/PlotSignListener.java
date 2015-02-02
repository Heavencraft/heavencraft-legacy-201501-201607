package fr.heavencraft.heavencrea.plots;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencrea.CreaPermissions;
import fr.heavencraft.heavencrea.HeavenCrea;
import fr.heavencraft.heavencrea.generator.CreativeChunkGenerator;
import fr.heavencraft.heavencrea.users.User;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlotSignListener extends AbstractSignListener
{
	private static final int SMALL_PRICE = 500;
	private static final int MEDIUM_PRICE = 2500;
	private static final int LARGE_PRICE = 10000;

	private final HeavenCrea plugin;
	private final HeavenGuard regionPlugin;

	public PlotSignListener(HeavenCrea plugin, HeavenGuard regionPlugin)
	{
		super(plugin, "Parcelle", CreaPermissions.PARCELLE_SIGN);

		this.plugin = plugin;
		this.regionPlugin = regionPlugin;
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		final Location location = event.getBlock().getLocation();

		final Collection<Region> regions = regionPlugin.getRegionProvider().getRegionsAtLocation(location.getWorld().getName(), //
				location.getBlockX(), location.getBlockY(), location.getBlockZ());

		if (regions.size() != 0)
			throw new HeavenException("Une protection existe déjà ici.");

		final Plot plot = getPlotAt(location);

		event.setLine(1, plot.price + " jetons");

		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		// Get the plot
		final Plot plot = getPlotAt(sign.getLocation());

		// Take the money
		final User user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
		user.updateBalance(-plot.price);

		// Create the region
		final Region region = createRegion(player, sign.getWorld().getName(), plot);

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

		sign.getBlock().breakNaturally();
		plugin.sendMessage(player, "Vous venez d'acheter la parcelle {%1$s} pour {%2$s} jetons.", region.getName(), plot.price);
	}

	private Region createRegion(Player player, String world, final Plot plot) throws HeavenException
	{
		final RegionProvider regionProvider = regionPlugin.getRegionProvider();
		String regionName;
		int i = 1;

		do
		{
			regionName = "parcelle_" + player.getName() + "_" + i++;
		}
		while (regionProvider.regionExists(regionName));

		final Region region = regionProvider.createRegion(regionName, world,//
				plot.minX, 0, plot.minZ, //
				plot.maxX, 0xFF, plot.maxZ);

		region.addMember(player.getUniqueId(), true);
		return region;
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
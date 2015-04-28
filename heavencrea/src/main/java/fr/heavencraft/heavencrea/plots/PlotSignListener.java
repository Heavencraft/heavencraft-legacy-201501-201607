package fr.heavencraft.heavencrea.plots;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencrea.CreaPermissions;
import fr.heavencraft.heavencrea.HeavenCrea;
import fr.heavencraft.heavencrea.users.CreativeUser;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlotSignListener extends AbstractPlotSignListener
{
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

		final Collection<Region> regions = regionPlugin.getRegionProvider().getRegionsAtLocation(
				location.getWorld().getName(), //
				location.getBlockX(), location.getBlockY(), location.getBlockZ());

		if (regions.size() != 0)
			throw new HeavenException("Une protection existe déjà ici.");

		final Plot plot = getNormalPlotAt(location);

		event.setLine(1, plot.price + " jetons");

		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		// Get the plot
		final Plot plot = getNormalPlotAt(sign.getLocation());

		// Take the money
		final CreativeUser user = plugin.getUserProvider().getUserByUniqueId(player.getUniqueId());
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
		plugin.sendMessage(player, "Vous venez d'acheter la parcelle {%1$s} pour {%2$s} jetons.",
				region.getName(), plot.price);
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
}
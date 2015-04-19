package fr.heavencraft.heavencrea.plots;

import java.sql.Timestamp;
import java.util.Calendar;
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
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;
import fr.heavencraft.heavenguard.bukkit.RegionUtil;

public class TalentSignListener extends AbstractPlotSignListener
{
	private final HeavenCrea plugin;
	private final HeavenGuard regionPlugin;

	public TalentSignListener(HeavenCrea plugin, HeavenGuard regionPlugin)
	{
		super(plugin, "Talent", CreaPermissions.PARCELLE_SIGN);

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

		event.setLine(1, "Parcelle pour");
		event.setLine(2, "l'admission");
		event.setLine(3, "en map talent.");

		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		// Get the plot
		final Plot plot = getTalentPlotAt(sign.getLocation());

		// Create the region
		final Region region = createRegion(player, sign.getWorld().getName(), plot);

		// Set redstone border
		for (int x = plot.minX; x <= plot.maxX; x++)
		{
			Bukkit.getWorld("world_creative").getBlockAt(x, 51, plot.minZ).setType(Material.LAPIS_BLOCK);
			Bukkit.getWorld("world_creative").getBlockAt(x, 51, plot.maxZ).setType(Material.LAPIS_BLOCK);
		}
		for (int z = plot.minZ; z <= plot.maxZ; z++)
		{
			Bukkit.getWorld("world_creative").getBlockAt(plot.minX, 51, z).setType(Material.LAPIS_BLOCK);
			Bukkit.getWorld("world_creative").getBlockAt(plot.maxX, 51, z).setType(Material.LAPIS_BLOCK);
		}

		sign.getBlock().breakNaturally();
		plugin.sendMessage(player,
				"Vous venez de recevoir la parcelle {%1$s}. Celle-ci sera supprimée dans 3 semaines.",
				region.getName(), plot.price);
	}

	private Region createRegion(Player player, String world, final Plot plot) throws HeavenException
	{
		final RegionProvider regionProvider = regionPlugin.getRegionProvider();
		final String regionName = "talent_" + player.getName();

		if (regionProvider.regionExists(regionName))
			throw new HeavenException("Vous avez déjà une parcelle Talent.");

		final Region region = regionProvider.createRegion(regionName, world,//
				plot.minX, 0, plot.minZ, //
				plot.maxX, 0xFF, plot.maxZ);

		region.addMember(player.getUniqueId(), true);
		RegionUtil.saveState(region);

		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 21);
		region.getFlagHandler().setTimestampFlag(Flag.REMOVE_TIMESTAMP, new Timestamp(cal.getTimeInMillis()));
		return region;
	}

	@Override
	protected void onSignBreak(Player player, Sign sign) throws HeavenException
	{
	}
}
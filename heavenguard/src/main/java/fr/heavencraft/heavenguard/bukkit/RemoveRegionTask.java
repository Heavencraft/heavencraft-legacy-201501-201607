package fr.heavencraft.heavenguard.bukkit;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;

public class RemoveRegionTask extends BukkitRunnable
{
	private static final HeavenLog log = HeavenLog.getLogger(RemoveRegionTask.class);
	private static final long PERIOD = 200; // 20 * 10 ticks

	private final RegionProvider regionProvider;

	public RemoveRegionTask(HeavenGuard plugin)
	{
		regionProvider = plugin.getRegionProvider();

		runTaskTimer(plugin, PERIOD, PERIOD);
	}

	@Override
	public void run()
	{
		final Date now = new Date();

		for (final World world : Bukkit.getWorlds())
		{
			for (final Region region : regionProvider.getRegionsInWorld(world.getName()))
			{
				try
				{
					final Timestamp remove = region.getFlagHandler().getTimestampFlag(Flag.REMOVE_TIMESTAMP);
					if (remove != null && remove.before(now))
					{
						final String regionName = region.getName();

						final byte[] state = region.getFlagHandler().getByteArrayFlag(Flag.STATE);
						if (state != null)
						{
							WorldEditUtil.load(state, world,
									new Location(world, region.getMinX(), region.getMinY(), region.getMinZ()),
									new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ()));
							log.info("Region %1$s removed.", regionName);
						}

						regionProvider.deleteRegion(regionName);
						log.info("Region %1$s removed.", regionName);
					}
				}
				catch (final HeavenException | IOException ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}
}
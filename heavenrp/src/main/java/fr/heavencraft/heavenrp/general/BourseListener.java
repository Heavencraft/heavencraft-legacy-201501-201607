package fr.heavencraft.heavenrp.general;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import fr.heavencraft.utils.DevUtil;
import fr.heavencraft.utils.WorldGuardUtil;

/*
 * Autorise le placement et la destruction de panneaux mais pas de blocs dans certaines régions
 */
public class BourseListener implements Listener
{
	private static final String[] regions = { "bourse", "bourse3" };
	private final WorldGuardPlugin wg;

	public BourseListener()
	{
		DevUtil.registerListener(this);
		wg = WorldGuardUtil.getWorldGuard();
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event)
	{
		if (event.getPlayer().isOp())
			return;

		Block block = event.getBlock();

		switch (block.getType())
		{
			case SIGN:
			case SIGN_POST:
			case WALL_SIGN:
				return;

			default:
				break;
		}

		for (String region : regions)
		{
			ProtectedRegion pr = wg.getRegionManager(block.getWorld()).getRegion(region);

			if (pr != null && pr.contains(block.getX(), block.getY(), block.getZ()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}
}

package fr.heavencraft.heavenrp.general;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.utils.RPUtils;

/*
 * Autorise le placement et la destruction de panneaux mais pas de blocs dans certaines régions
 */
public class BourseListener extends AbstractListener<HeavenPlugin>
{
	private static final String[] regions = { "bourse", "bourse3" };
	private final WorldGuardPlugin wg;

	public BourseListener(HeavenPlugin plugin)
	{
		super(plugin);
		wg = RPUtils.getWorldGuard();
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

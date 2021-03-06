package fr.heavencraft.heavenguard.bukkit.listeners;

import java.util.Iterator;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;

public class ProtectionEnvironmentListener extends AbstractListener<BukkitHeavenGuard>
{
	private final BukkitHeavenGuard plugin;

	public ProtectionEnvironmentListener(BukkitHeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	/*
	 * BlockEvent
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockBurn(BlockBurnEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (isProtected(event.getBlock()))
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockIgnite(BlockIgniteEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		if (event.getPlayer() != null) // Done by ProtectionPlayerListener
			return;

		if (isProtected(event.getBlock()))
			event.setCancelled(true);
	}

	/*
	 * Stop pistons
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		log.debug(event.getClass().getSimpleName() + " " + event.getBlock().getType());

		final BlockFace direction = event.getDirection();

		for (final Block block : event.getBlocks())
		{
			if (!areInSameRegion(block, block.getRelative(direction)))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	private boolean areInSameRegion(Block block1, Block block2)
	{
		return plugin.getRegionManager().areInSameRegion(block1.getWorld().getName(), //
				block1.getX(), block1.getY(), block1.getZ(), //
				block2.getX(), block2.getY(), block2.getZ());
	}

	// To stop water : BlockFromToEvent

	/*
	 * EntityEvent
	 */

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	private void onEntityChangeBlock(EntityChangeBlockEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		// BUGFIX : Pour faire tomber le sable, le gravier, etc...
		if (event.getEntityType() == EntityType.FALLING_BLOCK)
			return;

		// BUGFIX : Pour que les moutons puissent se nourrir
		if (event.getEntityType() == EntityType.SHEEP)
			return;

		if (isProtected(event.getBlock()))
			event.setCancelled(true);
	}

	// @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	// private void onEntityInteract(EntityInteractEvent event)
	// {
	// log.info(event.getClass().getSimpleName());
	//
	// if (event.getEntityType() == EntityType.PLAYER)
	// return;
	//
	// if (isProtected(event.getBlock()))
	// event.setCancelled(true);
	// }

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityExplode(EntityExplodeEvent event)
	{
		log.debug(event.getClass().getSimpleName());

		for (final Iterator<Block> it = event.blockList().iterator(); it.hasNext();)
		{
			if (isProtected(it.next()))
				it.remove();
		}
	}

	private boolean isProtected(Block block)
	{
		return plugin.getRegionManager().isProtectedAgainstEnvironment(block.getWorld().getName(), block.getX(),
				block.getY(), block.getZ());
	}
}
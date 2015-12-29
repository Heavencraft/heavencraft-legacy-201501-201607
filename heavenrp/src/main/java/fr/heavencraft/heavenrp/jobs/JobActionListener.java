package fr.heavencraft.heavenrp.jobs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class JobActionListener extends AbstractListener<HeavenPlugin>
{
	protected JobActionListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	private void dispatchJobAction(Player player, JobAction action)
	{
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event)
	{
		dispatchJobAction(event.getPlayer(), new JobAction(JobActionType.BREAK, event.getBlock().getType()));
	}

	@EventHandler(ignoreCancelled = true)
	private void onPlayerFish(PlayerFishEvent event)
	{
		if (event.getState() != State.CAUGHT_FISH)
			return;

		dispatchJobAction(event.getPlayer(), new JobAction(JobActionType.FISH, event.getCaught().getType()));
	}
}
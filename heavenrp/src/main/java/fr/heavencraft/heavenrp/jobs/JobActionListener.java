package fr.heavencraft.heavenrp.jobs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.jobs.actions.JobAction;
import fr.heavencraft.heavenrp.jobs.actions.JobActionType;

public class JobActionListener extends AbstractListener<HeavenPlugin>
{
	protected JobActionListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	private void dispatchJobAction(Player player, JobActionType type)
	{
	}

	@EventHandler(ignoreCancelled = true)
	private void onBlockBreak(BlockBreakEvent event)
	{
		final JobAction action = new JobAction(JobActionType.BREAK, event.getBlock().getType());
	}
}
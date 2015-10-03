package fr.heavencraft.heavencrea;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class AntiLagListener extends AbstractListener<HeavenPlugin>
{
	public AntiLagListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler
	private void onBlockExplode(BlockExplodeEvent event)
	{
		event.setCancelled(true);
	}

	@EventHandler
	private void onEntityExplode(EntityExplodeEvent event)
	{
		event.setCancelled(true);
	}
}
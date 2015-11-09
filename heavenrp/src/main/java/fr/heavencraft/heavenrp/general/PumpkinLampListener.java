package fr.heavencraft.heavenrp.general;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class PumpkinLampListener extends AbstractListener<HeavenPlugin>
{
	public PumpkinLampListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event)
	{
		Block block = event.getBlock();

		if (!block.getType().equals(Material.PUMPKIN) || !block.isBlockIndirectlyPowered())
			return;

		block.setType(Material.JACK_O_LANTERN);
	}

	@EventHandler
	public void onBlockRedstoneChange(BlockRedstoneEvent event)
	{
		Block block = event.getBlock();

		switch (block.getType())
		{
			case PUMPKIN:
				if (block.isBlockIndirectlyPowered())
				{
					byte d = block.getData();
					block.setType(Material.JACK_O_LANTERN);
					block.setData(d);
				}
				break;
			case JACK_O_LANTERN:
				if (!block.isBlockIndirectlyPowered())
				{
					byte d = block.getData();
					block.setType(Material.PUMPKIN);
					block.setData(d);
				}
				break;
			default:
				break;
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Block block = event.getBlock();

		if (block.getType() != Material.JACK_O_LANTERN || !block.isBlockIndirectlyPowered())
			return;

		block.setType(Material.PUMPKIN);
		block.breakNaturally();
	}
}

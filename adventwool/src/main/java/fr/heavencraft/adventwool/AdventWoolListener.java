package fr.heavencraft.adventwool;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AdventWoolListener implements Listener {
	
	AdventWoolPlugin _plugin;
	
	public AdventWoolListener(AdventWoolPlugin plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
		_plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{

		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
		{
			Block clickedBlock = event.getClickedBlock();

			if (clickedBlock.getType() == Material.WOOL)
			{
				//_plugin.getEventsManager().getEasterWool().onWoolClick(event.getPlayer(), clickedBlock);
				_plugin.getAdventWool().onWoolClick(event.getPlayer(), clickedBlock);
			}
		}

		
	}

}

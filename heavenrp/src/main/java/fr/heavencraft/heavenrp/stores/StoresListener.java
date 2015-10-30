package fr.heavencraft.heavenrp.stores;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.utils.ChatUtil;

public class StoresListener implements Listener
{

	HeavenRP _plugin;

	public StoresListener(HeavenRP plugin)
	{
		Bukkit.getPluginManager().registerEvents(this, plugin);
		_plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		_plugin.getStoresManager().onBlockDestroyed(event.getBlock());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onSignChange(SignChangeEvent event) throws HeavenException
	{
		String firstLine = event.getLine(0);

		if (firstLine.equalsIgnoreCase("[coffre]"))
			_plugin.getStoresManager().onCreateStock(event);

		else if (firstLine.equalsIgnoreCase("[magasin]"))
			_plugin.getStoresManager().onCreateStore(event, false);

		else if (firstLine.equalsIgnoreCase("[achat]"))
			_plugin.getStoresManager().onCreateStore(event, true);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) throws HeavenException
	{
		Player player = event.getPlayer();

		try
		{
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				Block clickedBlock = event.getClickedBlock();
				BlockState blockState = clickedBlock.getState();
				if (blockState instanceof Sign)
				{
					Sign sign = (Sign) blockState;
					if (sign.getLine(0).endsWith("[Magasin]") || sign.getLine(0).endsWith("[Achat]"))
					{
						_plugin.getStoresManager().useStore(player, clickedBlock, sign);
						event.setCancelled(true);
					}
				}
			}

			if (event.isCancelled())
				return;
		}
		catch (HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
		}
	}
}

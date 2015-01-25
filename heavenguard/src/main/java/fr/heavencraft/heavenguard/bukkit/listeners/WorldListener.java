package fr.heavencraft.heavenguard.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenguard.api.GlobalRegion;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class WorldListener extends AbstractListener
{
	private final HeavenGuard plugin;

	protected WorldListener(HeavenGuard plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler
	private void onWorldLoad(WorldLoadEvent event)
	{
		final GlobalRegion world = plugin.getRegionProvider().getGlobalRegion(event.getWorld().getName());

		log.info("Loaded %1$s", world);
	}
}
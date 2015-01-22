package fr.heavencraft.heavenguard.bukkit.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavenguard.api.GlobalRegion;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class WorldListener implements Listener
{
	private static final HeavenLog log = HeavenLog.getLogger(WorldListener.class);

	@EventHandler
	private void onWorldLoad(WorldLoadEvent event)
	{
		final GlobalRegion world = HeavenGuard.getRegionProvider().getGlobalRegion(event.getWorld().getName());

		log.info("Loaded %1$s", world);
	}
}
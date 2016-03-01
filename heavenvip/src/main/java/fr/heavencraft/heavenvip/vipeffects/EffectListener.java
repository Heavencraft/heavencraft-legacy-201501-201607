package fr.heavencraft.heavenvip.vipeffects;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.utils.particles.ParticleUtils;
import fr.heavencraft.heavenvip.HeavenVIP;

public class EffectListener extends AbstractListener<HeavenVIP>
{
	public EffectListener(HeavenVIP plugin)
	{
		super(plugin);
	}
	
	@EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
		// A player has moved
		final Player walker = event.getPlayer();
		ArrayList<Integer> effects = EffectCache.getEffectsByUUID(walker.getUniqueId().toString());
		if(effects == null)
			return;
		// For each element, show particle effect
		for(int i = 0; i < effects.size(); i++) 
		{
			ParticleUtils.fromId(effects.get(i)).display(0.5f, 0.5f, 0.5f, 0, 10, walker.getLocation(), 50);
		}
	}
	
	@EventHandler   
	public void onPlayerJoin(PlayerJoinEvent event) {
		EffectCache.updateCache(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		EffectCache.invalidateCache(event.getPlayer());
	}
}

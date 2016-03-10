package fr.heavencraft.heavenvip.movments;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffectType;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.utils.particles.ParticleEffect;
import fr.heavencraft.heavenvip.HeavenVIP;

public class MovmentEffectListener extends AbstractListener<HeavenVIP>
{
	private int EFFECT_VISIBILITY_RANGE = 50;
	public MovmentEffectListener(HeavenVIP plugin)
	{
		super(plugin);
	}
	
	final boolean disableOnSpinning = true;
	
	/**
	 * when the player moves, show them particles
	 * @param event
	 */
	@EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
		final Player walker = event.getPlayer();
		// Is the player vanished?
		if(walker.isSneaking() || walker.hasPotionEffect(PotionEffectType.INVISIBILITY))
			return;
		// Check if we are spinning
		if(disableOnSpinning 
				&& event.getFrom().getBlockX() == event.getTo().getBlockX()
				&& event.getFrom().getBlockY() == event.getTo().getBlockY()
				&& event.getFrom().getBlockZ() == event.getTo().getBlockZ())
				return;
		// Start applying effects
		ArrayList<AppliedDescriptorProperties> effects = MovmentEffectCache.getEffectsByUUID(walker.getUniqueId());
		if(effects == null)
			return;
		// For each element, show particle effect
		for(int i = 0; i < effects.size(); i++) 
		{
			AppliedDescriptorProperties aep = effects.get(i);
			ParticleEffect pu = aep.getEffect();
			// Has custom color?
			if (aep.getOrdinaryColor() != null) {
				// Yes, it's a normal color
				pu.display(aep.getOrdinaryColor(), walker.getLocation(), EFFECT_VISIBILITY_RANGE);
			} else if (aep.getNoteColor() != null) {
				// Yes, it's a note color
				pu.display(aep.getNoteColor(), walker.getLocation(), EFFECT_VISIBILITY_RANGE);
			} else {
				// No color
				pu.display(0f, 0f, 0f, aep.getSpeed(), aep.getAmount(), walker.getLocation(), EFFECT_VISIBILITY_RANGE);
			}
		}
	}
	
	/**
	 * Register User in  effect cache
	 * @param event
	 */
	@EventHandler   
	public void onPlayerJoin(PlayerJoinEvent event) {
		MovmentEffectCache.updateCache(event.getPlayer());
	}
	
	/**
	 * Unregister user from effect cache
	 * @param event
	 */
	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		MovmentEffectCache.invalidateCache(event.getPlayer());
	}
	
	private static UUID myUUID = UUID.fromString("1d396b17-99f9-42c7-82c6-7bf0bbfb38b4");
	/**
	 * Some magic things
	 * @param event
	 */
	@EventHandler
	public void onPlayerSneat(PlayerToggleSneakEvent event) {
		if(event.getPlayer().getUniqueId().equals(MovmentEffectListener.myUUID)) {
			EffectDescriptorUtils.SpawnEing(event.getPlayer(), new ParticleEffect.OrdinaryColor(Color.PURPLE), new ParticleEffect.OrdinaryColor(Color.PURPLE), new ParticleEffect.OrdinaryColor(Color.SILVER));
		}
	}
}

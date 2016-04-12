package fr.heavencraft.heavenrp.scrolls;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.particles.ParticleEffect;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPGLocks;

public class TeleportationScroll extends Scroll
{

	private final Location destination;

	protected TeleportationScroll(String name, List<String> lore, Location loc)
	{		
		super(name, lore);
		destination = loc;
	}

	@Override
	public void executeScroll(Player player) throws HeavenException
	{
		// Can we teleport?
		RPGLocks.getInstance().canTeleport(player);
		
		// Alter player vision
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 255, 255));
		player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 255, 255));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 255, 255));
		
		// Wait before teleport
		new BukkitRunnable() {
			@Override
			public void run() {
				ParticleEffect.SPELL.display((float)10, (float)0, (float)0, (float)0, 10,player.getPlayer().getLocation(), 1000);
				
				player.getPlayer().teleport(destination);
				
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
				
			}	
		}.runTaskLater(HeavenRP.getInstance(), 20);
		
		// Display effect when arrived
		new BukkitRunnable() {
			@Override
			public void run() {
				ParticleEffect.FIREWORKS_SPARK.display((float)10, (float)0, (float)0, (float)0, 10,player.getPlayer().getLocation(), 1000);
				player.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
			}	
		}.runTaskLater(HeavenRP.getInstance(), 40);	
	}

}

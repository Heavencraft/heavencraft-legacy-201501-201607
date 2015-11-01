package fr.heavencraft.rpg.Parchemins.Parchemins;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.Utils.ParticleEffect;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.Parchemins.IParchemin;
import fr.heavencraft.rpg.player.RPGPlayer;

public class ParcheminLePetitPetDuNord implements IParchemin{

	public int RPGexpieirence() {
		// L'XP nécessaire a l'execution
		return 0;
	}
	
	public boolean canDo(RPGPlayer player) {
		if(player.getRPGXp() < RPGexpieirence())
			return false;
		return true;
	}

	public ItemStack getItem() {
		ItemStack parchemin = new ItemStack(Material.PAPER);
		ItemMeta met = parchemin.getItemMeta();
		met.setDisplayName(ChatColor.RED + "Le petit pet du nord");
		parchemin.setItemMeta(met);
		return parchemin;
	}

	private int counter = 0;
	public void executeParchemin(final RPGPlayer player) {
		counter = 0;
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 255, 255));
		new BukkitRunnable() {
			@Override
			public void run() {
				ParticleEffect.PORTAL.display((float)10, (float)0, (float)0, (float)0, 10,player.getPlayer().getLocation(), 1000);
				counter += 1;
				if(counter >= 5)
					this.cancel();
			}
			
		}.runTask(HeavenRPG.getInstance());
		
		new BukkitRunnable() {
			@Override
			public void run() {
				
				ParticleEffect.FIREWORKS_SPARK.display((float)10, (float)0, (float)0, (float)0, 10,player.getPlayer().getLocation(), 1000);
				
				Fireball fb = player.getPlayer().launchProjectile(Fireball.class);
				fb.setIsIncendiary(false);
				fb.setFireTicks(5);
				fb.setShooter(player.getPlayer());
				fb.setVelocity(fb.getVelocity().multiply(2));
				fb.setYield(2);
				fb.setBounce(false);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}	
		}.runTaskLater(HeavenRPG.getInstance(), 60);	
	}

	

}

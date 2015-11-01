package fr.heavencraft.rpg.Parchemins.Parchemins;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.Utils.ParticleEffect;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.Parchemins.IParchemin;
import fr.heavencraft.rpg.player.RPGPlayer;

public class ParcheminMineDeCharbon implements IParchemin{

	public int RPGexpieirence() {
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
		met.setDisplayName(ChatColor.GREEN + "Portail: Mine de Charbon");
		parchemin.setItemMeta(met);
		return parchemin;
	}

	public void executeParchemin(final RPGPlayer player) {
		
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 255, 255));
		
		new BukkitRunnable() {
			@Override
			public void run() {
				ParticleEffect.SPELL.display((float)10, (float)0, (float)0, (float)0, 10,player.getPlayer().getLocation(), 1000);
				
				player.getPlayer().teleport(new Location(player.getPlayer().getWorld(), 2263, 75, -1619, 66,-6));
				
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
				
			}	
		}.runTaskLater(HeavenRPG.getInstance(), 20);	
		
		new BukkitRunnable() {
			@Override
			public void run() {
				ParticleEffect.FIREWORKS_SPARK.display((float)10, (float)0, (float)0, (float)0, 1,player.getPlayer().getLocation(), 1000);
				player.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
			}	
		}.runTaskLater(HeavenRPG.getInstance(), 40);	
	}

}

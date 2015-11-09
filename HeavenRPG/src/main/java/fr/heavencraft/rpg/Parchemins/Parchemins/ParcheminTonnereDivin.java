package fr.heavencraft.rpg.Parchemins.Parchemins;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import fr.heavencraft.Utils.ParticleEffect;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.Parchemins.IParchemin;
import fr.heavencraft.rpg.player.RPGPlayer;

public class ParcheminTonnereDivin implements IParchemin{

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
		met.setDisplayName(ChatColor.RED + "Tonnerre Divin");
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
				ParticleEffect.PORTAL.display((float)10, (float)0, (float)0, (float)0, 1,player.getPlayer().getLocation(), 1000);
				counter += 1;
				if(counter >= 5)
					this.cancel();
			}		
		}.runTask(HeavenRPG.getInstance());

		new BukkitRunnable() {
			@Override
			public void run() {

				ParticleEffect.WATER_SPLASH.display((float)10, (float)0, (float)0, (float)0, 10,player.getPlayer().getLocation(), 1000);
				// On récupère l'entitée visée a 45 blocs, sinon, on foudroie le lanceur.
				Entity e = getTarget(player.getPlayer(),45);
				if (e != null)
				{
					e.getWorld().strikeLightning(e.getLocation());
					e.setFireTicks(100);
					
				}
				else
				{
					player.getPlayer().getWorld().strikeLightning(player.getPlayer().getLocation());
					player.getPlayer().setFireTicks(40);
				}
				
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}	
		}.runTaskLater(HeavenRPG.getInstance(), 60);	
	}

	/**
	 * Retorune l'entitée en visuel
	 * @param player
	 * @param range la profondeur de blocs max.
	 * @return
	 */
	private Entity getTarget(Player player, int range) {
		Entity target = null;
		List<Entity> nearbyE = player.getNearbyEntities(range,  range, range);
		ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();

		for (Entity e : nearbyE) {
			if (e instanceof LivingEntity) {
				livingE.add((LivingEntity) e);
			}
		}

		BlockIterator bItr = new BlockIterator(player, range);
		Block block;
		Location loc;
		int bx, by, bz;
		double ex, ey, ez;
		// loop through player's line of sight
		while (bItr.hasNext()) {
			block = bItr.next();
			bx = block.getX();
			by = block.getY();
			bz = block.getZ();
			// check for entities near this block in the line of sight
			for (LivingEntity e : livingE) {
				loc = e.getLocation();
				ex = loc.getX();
				ey = loc.getY();
				ez = loc.getZ();
				if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
					// entity is close enough, set target and stop
					target = e;
					return target;
				}
			}
		}
		return target;

	}

}

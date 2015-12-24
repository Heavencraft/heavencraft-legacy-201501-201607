package fr.heavencraft.rpg.Parchemins.Parchemins;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.heavencraft.Utils.ParticleEffect;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.Parchemins.IParchemin;
import fr.heavencraft.rpg.player.RPGPlayer;

public class ParcheminPousseeQuantique implements IParchemin
{

	@Override
	public int RPGexpieirence()
	{
		return 0;
	}

	@Override
	public boolean canDo(RPGPlayer player)
	{
		if (player.getRPGXp() < RPGexpieirence())
			return false;
		return true;
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack parchemin = new ItemStack(Material.PAPER);
		ItemMeta met = parchemin.getItemMeta();
		met.setDisplayName(ChatColor.RED + "Poussée Quantique");
		parchemin.setItemMeta(met);
		return parchemin;
	}

	@Override
	public void executeParchemin(final RPGPlayer player)
	{

		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 255, 255));

		new BukkitRunnable()
		{
			@Override
			public void run()
			{

//				ParticleEffect.CLOUD.display(10, 0, 0, 0, 10, player.getPlayer().getLocation(), 1000);
//
//				List<Entity> entities = player.getPlayer().getNearbyEntities(6, 6, 6);
//				for (Entity e : entities)
//				{
//					Vector unitVector = e.getLocation().toVector()
//							.subtract(player.getPlayer().getLocation().toVector()).normalize();
//					// Set speed and push entity:
//					e.setVelocity(unitVector.multiply(4.0));
//				}

				player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
				player.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
			}
		}.runTaskLater(HeavenRPG.getInstance(), 20);
	}

}

package fr.heavencraft.rpg.Parchemins.Parchemins;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;

import fr.heavencraft.Utils.ParticleEffect;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.Parchemins.IParchemin;
import fr.heavencraft.rpg.player.RPGPlayer;

public class ParcheminDuChaineux implements IParchemin
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

		Block b = player.getPlayer().getTargetBlock((Set<Material>) null, 10);
		ApplicableRegionSet regions = HeavenRPG.getWorldGuard().getRegionManager(b.getWorld())
				.getApplicableRegions(b.getLocation());
		LocalPlayer lPlayer = HeavenRPG.getWorldGuard().wrapPlayer(player.getPlayer());
		return regions.canBuild(lPlayer);
	}

	@Override
	public ItemStack getItem()
	{
		ItemStack parchemin = new ItemStack(Material.PAPER);
		ItemMeta met = parchemin.getItemMeta();
		met.setDisplayName(ChatColor.DARK_PURPLE + "Le Champignionium");
		parchemin.setItemMeta(met);
		return parchemin;
	}

	@Override
	public void executeParchemin(final RPGPlayer player)
	{

		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 255, 255));
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 255, 255));

		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ParticleEffect.FIREWORKS_SPARK.display(10, 0, 0, 0, 10, player.getPlayer().getLocation(), 1000);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
				player.getPlayer().removePotionEffect(PotionEffectType.NIGHT_VISION);
				player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
			}
		}.runTaskLater(HeavenRPG.getInstance(), 10);
	}
}

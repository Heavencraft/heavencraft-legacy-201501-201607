package fr.heavencraft.heavenrp.provinces;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavenrp.RPGLocks;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;

public class ProvinceEffectTask extends BukkitRunnable
{
	private static final long PERIOD = 1200 + 3; // 5 min: 5 * 20 * 60 + 20 (+3 ticks to delay effects by 1/2sec.
	
	public ProvinceEffectTask(HeavenPlugin plugin)
	{
		runTaskTimer(plugin, PERIOD, PERIOD);
		HeavenLog.getLogger(getClass());
	}

	@Override
	public void run()
	{
		if (!ProvincesManager.applyEffects())
			return;

		Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		for (Player player : players)
		{
			try
			{
				RPGLocks.getInstance().canObtainProvinceEffect(player);
				User user = UserProvider.getUserByName(player.getName());
				Province prov = ProvincesManager.getProvinceByUser(user);
				if (prov == null)
					return;
				Collection<PotionEffect> effects = ProvincesManager.getEffects(prov);
				player.addPotionEffects(effects);
			}
			catch (HeavenException ex)
			{
				// The player is disallowed to obtain his effects.
			}
		}
	}
}

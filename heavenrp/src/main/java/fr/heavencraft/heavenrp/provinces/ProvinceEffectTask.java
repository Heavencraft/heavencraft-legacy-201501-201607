package fr.heavencraft.heavenrp.provinces;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;

public class ProvinceEffectTask extends BukkitRunnable
{
	private static final long PERIOD = 6100; // 5 min: 5 * 20 * 60 + 20   (+100 ticks to overlap effects by 1sec.

	private HeavenLog log;

	public ProvinceEffectTask(HeavenPlugin plugin)
	{
		runTaskTimer(plugin, PERIOD, PERIOD);
		log = HeavenLog.getLogger(getClass());
	}
	
	@Override
	public void run()
	{
		if(!ProvincesManager.applyEffects())
			return;
		try
		{
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			for (Player player : players)
			{
				User user = UserProvider.getUserByName(player.getName());
				Province prov = ProvincesManager.getProvinceByUser(user);
				if(prov == null)
					return;
				Collection<PotionEffect> effects = ProvincesManager.getEffects(prov);
				player.addPotionEffects(effects);
			}
		}
		catch (HeavenException ex)
		{
			ex.printStackTrace();
		}
	}

}

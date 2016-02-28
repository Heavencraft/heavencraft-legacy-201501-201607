package fr.heavencraft.heavenrp.economy;

import java.util.Calendar;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class MoneyTask extends BukkitRunnable
{
	private static final long PERIOD = 12000; // 10 minutes : 20 * 60 * 10 ticks

	private HeavenLog log;

	public MoneyTask(HeavenPlugin plugin)
	{
		runTaskTimer(plugin, PERIOD, PERIOD);
		log = HeavenLog.getLogger(getClass());
	}

	@Override
	public void run()
	{
		try
		{
			Collection<? extends Player> players = Bukkit.getOnlinePlayers();
			int amount = getAmount();

			log.info("[MoneyTask] giving %1$s po to %2$s players", amount, players.size());

			for (Player player : players)
			{
				if(!player.hasPermission(RPPermissions.PRIVILEGES_LOSS)) {
					User user = UserProvider.getUserByName(player.getName());
					QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, amount));
				}
			}
		}
		catch (HeavenException ex)
		{
			ex.printStackTrace();
		}
	}

	private static int getAmount()
	{
		Calendar date = Calendar.getInstance();

		switch (date.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.SATURDAY:
			case Calendar.SUNDAY:
				return date.get(Calendar.HOUR_OF_DAY) < 18 ? 2 : 3;

			default:
				return date.get(Calendar.HOUR_OF_DAY) < 18 ? 1 : 2;
		}
	}
}
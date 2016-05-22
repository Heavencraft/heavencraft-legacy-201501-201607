package fr.heavencraft.heavencore.users.balance;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavencore.users.UserProvider;

public class MoneyTask extends BukkitRunnable
{
	private final UserProvider<? extends UserWithBalance> userProvider;

	private final HeavenLog log;

	public MoneyTask(HeavenPlugin plugin, UserProvider<? extends UserWithBalance> userProvider, long period)
	{
		this.userProvider = userProvider;

		runTaskTimer(plugin, period, period);
		log = HeavenLog.getLogger(getClass());
	}

	@Override
	public void run()
	{
		final int amount = getAmount();
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		log.info("[MoneyTask] giving %1$s po to %2$s players", amount, players.size());

		for (final Player player : players)
			giveMoneyToPlayer(player, amount);
	}

	protected int getAmount()
	{
		return 1;
	}

	protected void giveMoneyToPlayer(Player player, int amount)
	{
		try
		{
			final UserWithBalance user = userProvider.getUserByUniqueId(player.getUniqueId());
			QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, amount, userProvider));
		}
		catch (final HeavenException ex)
		{
			ex.printStackTrace();
		}
	}
}
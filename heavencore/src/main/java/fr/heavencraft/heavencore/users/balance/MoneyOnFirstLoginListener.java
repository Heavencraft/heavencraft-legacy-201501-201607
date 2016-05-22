package fr.heavencraft.heavencore.users.balance;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.async.queries.BatchQuery;
import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.async.queries.Query;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.users.UpdateUserLastLoginQuery;
import fr.heavencraft.heavencore.users.UserProvider;
import fr.heavencraft.heavencore.users.balance.UpdateUserBalanceQuery;
import fr.heavencraft.heavencore.users.balance.UserWithBalance;
import fr.heavencraft.heavencore.utils.DateUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

/**
 * MoneyOnFirstLoginListener : Listener that give an amount of money to the player on the first login of the day.
 * 
 * @author lorgan
 */
public class MoneyOnFirstLoginListener extends AbstractListener<HeavenPlugin>
{
	private static final String MESSAGE = "Vous venez d'obtenir {%1$s %2$s} en vous connectant !";

	private final UserProvider<? extends UserWithBalance> userProvider;
	private final int moneyOnFirstLogin;

	public MoneyOnFirstLoginListener(HeavenPlugin plugin, UserProvider<? extends UserWithBalance> userProvider,
			int moneyOnFirstLogin)
	{
		super(plugin);

		this.userProvider = userProvider;
		this.moneyOnFirstLogin = moneyOnFirstLogin;
	}

	@EventHandler
	private void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();
		final UserWithBalance user = userProvider.getUserByUniqueId(player.getUniqueId());

		if (!DateUtil.isToday(user.getLastLogin()))
			onFirstLogin(player, user);

		else
			QueriesHandler.addQuery(new UpdateUserLastLoginQuery(user, userProvider));
	}

	private void onFirstLogin(Player player, UserWithBalance user)
	{
		final List<Query> queries = new ArrayList<Query>();
		queries.add(new UpdateUserBalanceQuery(user, moneyOnFirstLogin, userProvider));
		queries.add(new UpdateUserLastLoginQuery(user, userProvider));
		QueriesHandler.addQuery(new BatchQuery(queries)
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, MESSAGE, moneyOnFirstLogin, user.getCurrencyName());
			}
		});
	}
}
package fr.heavencraft.heavenrp.economy;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.async.queries.BatchQuery;
import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.async.queries.Query;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.bankaccounts.UpdateBankAccountBalanceQuery;
import fr.heavencraft.heavenrp.database.transactions.AddTransactionQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserBalanceQuery;
import fr.heavencraft.heavenrp.database.users.UpdateUserLastLoginQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.heavenrp.utils.RPUtils;

public class EconomyListener implements Listener
{
	public EconomyListener()
	{
		Bukkit.getPluginManager().registerEvents(this, HeavenRP.getInstance());
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		final Player player = event.getPlayer();
		String playerName = player.getName();

		User user = UserProvider.getUserByName(playerName);

		if (!RPUtils.isToday(user.getLastLogin()))
		{
			BankAccount account = BankAccountsManager.getBankAccount(playerName, BankAccountType.USER);
			final int benefit;

			if (account.getBalance() >= 25000)
				benefit = 25;
			else
				benefit = (int) (account.getBalance() * 0.001D);

			List<Query> queries = new ArrayList<Query>();
			queries.add(new UpdateUserBalanceQuery(user, 5));
			if (benefit > 0)
			{
				queries.add(new UpdateBankAccountBalanceQuery(account, benefit));
				queries.add(new AddTransactionQuery(account, benefit, "Intérets journaliers"));
			}
			queries.add(new UpdateUserLastLoginQuery(user));
			QueriesHandler.addQuery(new BatchQuery(queries)
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(player,
							ChatColor.AQUA + "Vous venez d'obtenir 5 pièces d'or en vous connectant !");

					if (benefit > 0)
					{
						ChatUtil.sendMessage(player,
								ChatColor.AQUA + "Votre livret vous a rapporté %1$s pièces d'or.", benefit);
					}
				}
			});
		}
		else
		{
			QueriesHandler.addQuery(new UpdateUserLastLoginQuery(user));
		}
	}

}
package fr.heavencraft.heavenfun.economy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenfun.BukkitHeavenFun;

public class EconomyListener extends AbstractListener<BukkitHeavenFun>
{
	protected EconomyListener(BukkitHeavenFun plugin)
	{
		super(plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws HeavenException
	{
		// final Player player = event.getPlayer();
		// final String playerName = player.getName();
		//
		// final User user = UserProvider.getUserByName(playerName);
		//
		// if (!RPUtils.isToday(user.getLastLogin()))
		// {
		// final BankAccount account = BankAccountsManager.getBankAccount(playerName, BankAccountType.USER);
		// final int benefit;
		//
		// if (account.getBalance() >= 25000)
		// benefit = 25;
		// else
		// benefit = (int) (account.getBalance() * 0.001D);
		//
		// final List<Query> queries = new ArrayList<Query>();
		// queries.add(new UpdateUserBalanceQuery(user, 5));
		// if (benefit > 0)
		// {
		// queries.add(new UpdateBankAccountBalanceQuery(account, benefit));
		// queries.add(new AddTransactionQuery(account, benefit, "Intérets journaliers"));
		// }
		// queries.add(new UpdateUserLastLoginQuery(user));
		// QueriesHandler.addQuery(new BatchQuery(queries)
		// {
		// @Override
		// public void onSuccess()
		// {
		// ChatUtil.sendMessage(player,
		// ChatColor.AQUA + "Vous venez d'obtenir 5 pièces d'or en vous connectant !");
		//
		// if (benefit > 0)
		// {
		// ChatUtil.sendMessage(player, ChatColor.AQUA + "Votre livret vous a rapporté %1$s pièces d'or.",
		// benefit);
		// }
		// }
		// });
		// }
		// else
		// {
		// QueriesHandler.addQuery(new UpdateUserLastLoginQuery(user));
		// }
	}
}
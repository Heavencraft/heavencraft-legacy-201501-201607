package fr.heavencraft.heavenrp.economy;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.MoneyTransfertQuery;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class LivretSignListener extends AbstractBankAccountSignListener implements Listener
{
	private final Collection<String> deposants = new HashSet<String>();
	private final Collection<String> retirants = new HashSet<String>();

	public LivretSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Livret", RPPermissions.LIVRET_SIGN);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	private static String buildTransactionLog(Player player, boolean isDepot)
	{
		return (isDepot ? "Dépot de " : "Retrait de ") + player.getName();
	}

	@Override
	protected void onConsultSignClick(Player player) throws HeavenException
	{
		ChatUtil.sendMessage(player, "{Trésorier} : Vous avez {%1$d} pièces d'or sur votre livret.",
				BankAccountsManager.getBankAccount(player.getName(), BankAccountType.USER).getBalance());
	}

	@Override
	protected void onDepositSignClick(Player player) throws HeavenException
	{
		ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous déposer ?");
		deposants.add(player.getName());
	}

	@Override
	protected void onWithdrawSignClick(Player player) throws HeavenException
	{
		ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous retirer ?");
		retirants.add(player.getName());
	}

	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		String playerName = player.getName();
		boolean isDepot = false;

		if (deposants.contains(playerName))
		{
			deposants.remove(playerName);
			isDepot = true;
		}
		else if (retirants.contains(playerName))
		{
			retirants.remove(playerName);
			isDepot = false;
		}
		else
		{
			return;
		}

		event.setCancelled(true);

		try
		{
			int delta = DevUtil.toUint(event.getMessage());

			User user = UserProvider.getUserByName(playerName);
			BankAccount bank = BankAccountsManager.getBankAccount(playerName, BankAccountType.USER);

			QueriesHandler.addQuery(new MoneyTransfertQuery(isDepot ? user : bank, isDepot ? bank : user, delta,
					buildTransactionLog(player, isDepot))
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(player, "{Trésorier} : L'opération a bien été effectuée.");
				}

				@Override
				public void onHeavenException(HeavenException ex)
				{
					ChatUtil.sendMessage(player, ex.getMessage());
				}
			});
		}
		catch (HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
		}
	}
}
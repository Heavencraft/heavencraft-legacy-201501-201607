package fr.heavencraft.heavenrp.economy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class LivretProSignListener extends AbstractBankAccountSignListener implements Listener
{

	private final Map<String, Integer> deposants = new HashMap<String, Integer>();
	private final Map<String, Integer> retirants = new HashMap<String, Integer>();

	public LivretProSignListener(HeavenPlugin plugin)
	{
		super(plugin, "LivretPro", RPPermissions.LIVRETPRO_SIGN);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	private static String buildTransactionLog(Player player, boolean isDepot)
	{
		return (isDepot ? "Dépot de " : "Retrait de ") + player.getName();
	}

	@Override
	protected void onConsultSignClick(Player player) throws HeavenException
	{
		String playerName = player.getName();

		List<BankAccount> accounts = BankAccountsManager.getAccountByOwner(playerName);

		if (accounts.size() == 0)
			throw new HeavenException("{Trésorier} : Vous n'avez accès à aucun livret...");

		ChatUtil.sendMessage(player, "{Trésorier} : Voici la liste de vos livrets :");

		for (BankAccount account : accounts)
			ChatUtil.sendMessage(player, "{%1$s} (%2$s) : {%3$s} pièces d'or", account.getId(), account.getName(),
					account.getBalance());
	}

	@Override
	protected void onDepositSignClick(Player player) throws HeavenException
	{
		String playerName = player.getName();

		if (!deposants.containsKey(playerName))
		{
			onConsultSignClick(player);
			ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous déposer ?");
			deposants.put(playerName, -1);
		}
	}

	@Override
	protected void onWithdrawSignClick(Player player) throws HeavenException
	{
		String playerName = player.getName();

		if (!retirants.containsKey(playerName))
		{
			onConsultSignClick(player);
			ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous retirer ?");
			retirants.put(playerName, -1);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		String playerName = player.getName();

		int accountId;
		boolean isDepot = false;

		if (deposants.containsKey(playerName))
		{
			accountId = deposants.get(playerName);
			isDepot = true;
		}
		else if (retirants.containsKey(playerName))
		{
			accountId = retirants.get(playerName);
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

			if (accountId == -1)
			{
				selectAccount(player, delta, isDepot ? deposants : retirants);
				return;
			}

			deposants.remove(playerName);
			retirants.remove(playerName);

			User user = UserProvider.getUserByName(playerName);
			BankAccount bank = BankAccountsManager.getBankAccountById(accountId);

			QueriesHandler.addQuery(new MoneyTransfertQuery(isDepot ? user : bank, isDepot ? bank : user, delta,
					buildTransactionLog(player, isDepot))
			{
				@Override
				public void onSuccess()
				{
					ChatUtil.sendMessage(player, "{Trésorier} : L'opération a été effectuée avec succès.");
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
			deposants.remove(playerName);
			retirants.remove(playerName);
			ChatUtil.sendMessage(player, ex.getMessage());
		}
	}

	private void selectAccount(Player player, int id, Map<String, Integer> list) throws HeavenException
	{
		BankAccount account = BankAccountsManager.getBankAccountById(id);

		if (!account.getOwners().contains(player))
			throw new HeavenException("{Trésorier} : Vous n'êtes pas propriétaire de ce compte.");

		list.put(player.getName(), id);

		if (list.equals(deposants))
			ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous déposer ?");
		else
			ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous retirer ?");
	}
}
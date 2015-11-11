package fr.heavencraft.heavenrp.economy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	enum LivretProAction
	{
		DEPOSIT,
		WITHDRAW,
		STATEMENT
	}

	private final Map<UUID, LivretProAction> pendingActions = new HashMap<UUID, LivretProAction>();
	private final Map<UUID, Integer> selectedAccounts = new HashMap<UUID, Integer>();

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
		pendingActions.put(player.getUniqueId(), LivretProAction.DEPOSIT);

		onConsultSignClick(player);
		ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous déposer ?");
	}

	@Override
	protected void onWithdrawSignClick(Player player) throws HeavenException
	{
		pendingActions.put(player.getUniqueId(), LivretProAction.WITHDRAW);

		onConsultSignClick(player);
		ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous retirer ?");
	}

	@Override
	protected void onStatementSignClick(Player player) throws HeavenException
	{
		pendingActions.put(player.getUniqueId(), LivretProAction.STATEMENT);

		onConsultSignClick(player);
		ChatUtil.sendMessage(player, "{Trésorier} : De quel livret voulez-vous le relevé ?");
	}

	@EventHandler(ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
	{
		final Player player = event.getPlayer();
		UUID uniqueId = player.getUniqueId();

		LivretProAction action = pendingActions.get(uniqueId);
		if (action == null)
			return;

		event.setCancelled(true);

		try
		{
			int input = DevUtil.toUint(event.getMessage());

			Integer accountId = selectedAccounts.get(uniqueId);
			if (accountId == null)
			{
				BankAccount account = getAccount(player, input);

				switch (action)
				{
					case DEPOSIT:
						selectedAccounts.put(player.getUniqueId(), account.getId());
						ChatUtil.sendMessage(player,
								"{Trésorier} : Combien de pièces d'or souhaitez-vous déposer ?");
						break;
					case WITHDRAW:
						selectedAccounts.put(player.getUniqueId(), account.getId());
						ChatUtil.sendMessage(player,
								"{Trésorier} : Combien de pièces d'or souhaitez-vous retirer ?");
						break;
					case STATEMENT:
						player.getInventory().addItem(createLastTransactionsBook(account, 3));
						clear(uniqueId);
						ChatUtil.sendMessage(player, "{Trésorier} : L'opération a été effectuée avec succès.");
						break;
				}
			}
			else
			{
				executeTransaction(player, accountId, action, input);
				clear(uniqueId);
			}
		}
		catch (HeavenException ex)
		{
			clear(uniqueId);
			ChatUtil.sendMessage(player, ex.getMessage());
		}
	}

	private static BankAccount getAccount(Player player, int id) throws HeavenException
	{
		BankAccount account = BankAccountsManager.getBankAccountById(id);

		if (!account.getOwners().contains(player))
			throw new HeavenException("{Trésorier} : Vous n'êtes pas propriétaire de ce compte.");

		return account;
	}

	private void executeTransaction(Player player, int accountId, LivretProAction action, int delta)
			throws HeavenException
	{
		User user = UserProvider.getUserByName(player.getName());
		BankAccount bank = BankAccountsManager.getBankAccountById(accountId);

		Object from, to;
		switch (action)
		{
			case DEPOSIT:
				from = user;
				to = bank;
				break;
			case WITHDRAW:
				from = bank;
				to = user;
				break;
			default:
				throw new HeavenException("Opération invalide.");
		}

		QueriesHandler.addQuery(new MoneyTransfertQuery(from, to, delta,
				buildTransactionLog(player, action == LivretProAction.DEPOSIT))
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

	private void clear(UUID uniqueId)
	{
		pendingActions.remove(uniqueId);
		selectedAccounts.remove(uniqueId);
	}
}
package fr.heavencraft.heavenrp.economy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.MoneyTransfertQuery;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class LivretProSignListener extends AbstractSignListener
{
	private static final String CONSULTER = "Consulter";
	private static final String DEPOSER = "Déposer";
	private static final String RETIRER = "Retirer";

	private final Map<String, Integer> deposants = new HashMap<String, Integer>();
	private final Map<String, Integer> retirants = new HashMap<String, Integer>();

	public LivretProSignListener(HeavenPlugin plugin)
	{
		super(plugin, "LivretPro", RPPermissions.LIVRETPRO_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event)
	{
		if (event.getLine(1).equalsIgnoreCase(CONSULTER))
		{
			event.setLine(1, ChatColor.BLUE + CONSULTER);
			return true;
		}

		else if (event.getLine(1).equalsIgnoreCase(DEPOSER))
		{
			event.setLine(1, ChatColor.BLUE + DEPOSER);
			return true;
		}

		else if (event.getLine(1).equalsIgnoreCase(RETIRER))
		{
			event.setLine(1, ChatColor.BLUE + RETIRER);
			return true;
		}

		else
			return false;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		String playerName = player.getName();

		if (sign.getLine(1).equals(ChatColor.BLUE + CONSULTER))
		{
			displayAccounts(player);
		}

		else if (sign.getLine(1).equals(ChatColor.BLUE + DEPOSER))
		{
			if (!deposants.containsKey(playerName))
			{
				displayAccounts(player);
				ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous déposer ?");
				deposants.put(playerName, -1);
			}
		}

		else if (sign.getLine(1).equals(ChatColor.BLUE + RETIRER))
		{
			if (!retirants.containsKey(playerName))
			{
				displayAccounts(player);
				ChatUtil.sendMessage(player, "{Trésorier} : Sur quel livret voulez-vous retirer ?");
				retirants.put(playerName, -1);
			}
		}
	}

	public void displayAccounts(Player player) throws HeavenException
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

			QueriesHandler.addQuery(new MoneyTransfertQuery(isDepot ? user : bank, isDepot ? bank : user, delta)
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

	@Override
	protected void onSignBreak(Player player, Sign sign) throws HeavenException
	{
	}
}
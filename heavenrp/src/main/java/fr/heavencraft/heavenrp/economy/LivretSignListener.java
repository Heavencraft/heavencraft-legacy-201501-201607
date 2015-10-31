package fr.heavencraft.heavenrp.economy;

import java.util.ArrayList;
import java.util.List;

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
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.MoneyTransfertQuery;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.utils.ChatUtil;

public class LivretSignListener extends AbstractSignListener
{
	private static final String CONSULTER = "Consulter";
	private static final String DEPOSER = "Déposer";
	private static final String RETIRER = "Retirer";

	private final List<String> deposants = new ArrayList<String>();
	private final List<String> retirants = new ArrayList<String>();

	public LivretSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Livret", RPPermissions.LIVRET_SIGN);
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
			ChatUtil.sendMessage(player, "{Trésorier} : Vous avez {%1$d} pièces d'or sur votre livret.",
					BankAccountsManager.getBankAccount(playerName, BankAccountType.USER).getBalance());
		}

		else if (sign.getLine(1).equals(ChatColor.BLUE + DEPOSER))
		{
			ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous déposer ?");

			if (!deposants.contains(playerName))
				deposants.add(playerName);
		}

		else if (sign.getLine(1).equals(ChatColor.BLUE + RETIRER))
		{
			ChatUtil.sendMessage(player, "{Trésorier} : Combien de pièces d'or souhaitez-vous retirer ?");

			if (!retirants.contains(playerName))
				retirants.add(playerName);
		}
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

			QueriesHandler.addQuery(new MoneyTransfertQuery(isDepot ? user : bank, isDepot ? bank : user, delta)
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

	@Override
	protected void onSignBreak(Player player, Sign sign) throws HeavenException
	{
	}
}
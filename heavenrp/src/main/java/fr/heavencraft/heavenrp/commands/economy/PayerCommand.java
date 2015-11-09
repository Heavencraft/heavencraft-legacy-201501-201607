package fr.heavencraft.heavenrp.commands.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.MoneyTransfertQuery;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class PayerCommand extends AbstractCommandExecutor
{
	private final static String MONEY_NOW = "Vous avez maintenant {%1$d} pièces d'or.";
	private final static String MONEY_BANK_NOW = "Vous avez maintenant {%1$d} pièces d'or sur le livret {%2$s}.";
	private final static String MONEY_GIVE = "Vous avez envoyé {%1$d} pièces d'or à {%2$s}.";
	private final static String MONEY_RECEIVE = "Vous avez reçu {%1$d} pièces d'or de {%2$s}.";

	public PayerCommand(HeavenRP plugin)
	{
		super(plugin, "payer");
	}

	private static String buildTransactionLog(Player player)
	{
		return "Paiement de " + player.getName();
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		if (args.length != 3)
		{
			sendUsage(player);
			return;
		}

		final BankAccount dest;

		if (args[0].equalsIgnoreCase("joueur"))
			dest = BankAccountsManager.getBankAccount(PlayerUtil.getExactName(args[1]), BankAccountType.USER);
		else if (args[0].equalsIgnoreCase("ville"))
			dest = BankAccountsManager.getBankAccount(args[1], BankAccountType.TOWN);
		else if (args[0].equalsIgnoreCase("entreprise"))
			dest = BankAccountsManager.getBankAccount(args[1], BankAccountType.ENTERPRISE);
		else
		{
			sendUsage(player);
			return;
		}

		if (dest.getOwnersNames().contains(player.getName()))
			throw new HeavenException(
					"Vous devez utiliser le guichet afin de faire des opérations sur votre compte");

		final int delta = DevUtil.toUint(args[2]);

		if (delta <= 0)
			throw new HeavenException("Le nombre {%1$s} est incorrect.", delta);

		final User sender = UserProvider.getUserByName(player.getName());

		QueriesHandler.addQuery(new MoneyTransfertQuery(sender, dest, delta, buildTransactionLog(player))
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, MONEY_GIVE, delta, dest.getName());
				ChatUtil.sendMessage(player, MONEY_NOW, sender.getBalance() - delta);

				ChatUtil.sendMessage(dest.getOwners(), MONEY_RECEIVE, delta, sender.getName());
				ChatUtil.sendMessage(dest.getOwners(), MONEY_BANK_NOW, dest.getBalance() + delta, dest.getName());

				log.info("%1$s sent %2$s po to bank account %3$s.", player.getName(), delta, dest.getName());
			}

			@Override
			public void onHeavenException(HeavenException ex)
			{
				ChatUtil.sendMessage(player, ex.getMessage());
			}
		});
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{payer} joueur <nom du joueur> <somme>");
		ChatUtil.sendMessage(sender, "/{payer} ville <nom de la ville> <somme>");
		ChatUtil.sendMessage(sender, "/{payer} entreprise <nom de l'entreprise> <somme>");
	}
}
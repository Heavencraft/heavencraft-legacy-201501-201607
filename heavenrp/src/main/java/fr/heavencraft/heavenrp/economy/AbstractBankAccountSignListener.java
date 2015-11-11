package fr.heavencraft.heavenrp.economy;

import java.util.Collection;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.BookBuilder;
import fr.heavencraft.heavencore.utils.DateUtil;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;
import fr.heavencraft.heavenrp.database.transactions.Transaction;
import fr.heavencraft.heavenrp.database.transactions.TransactionProvider;

public abstract class AbstractBankAccountSignListener extends AbstractSignListener
{
	private static final String CONSULT = "Consulter";
	private static final String DEPOSIT = "Déposer";
	private static final String WITHDRAW = "Retirer";
	private static final String STATEMENT = "Relevé";
	private static final String BLUE_CONSULT = ChatColor.BLUE + CONSULT;
	private static final String BLUE_DEPOSIT = ChatColor.BLUE + DEPOSIT;
	private static final String BLUE_WITHDRAW = ChatColor.BLUE + WITHDRAW;
	private static final String BLUE_STATEMENT = ChatColor.BLUE + STATEMENT;

	private final BookBuilder builder = new BookBuilder();

	public AbstractBankAccountSignListener(HeavenPlugin plugin, String tag, String permission)
	{
		super(plugin, tag, permission);
	}

	protected ItemStack createLastTransactionsBook(BankAccount account, int transactionsPerPage)
			throws HeavenException
	{
		Collection<Transaction> transactions = TransactionProvider.getLastTransactions(account);

		String today = DateUtil.toDateString(new Date());

		builder.setTitle(ChatColor.GREEN + "RC " + account.getName() + " " + today);
		builder.writeLine("Relevé de Compte");
		builder.writeLine("===============");
		builder.writeLine("");
		builder.writeLine("Compte: " + account.getName());
		builder.writeLine("Date: " + today);
		builder.writeLine("Solde: " + account.getBalance() + " po");
		builder.turnPage();

		int i = 0;
		for (Transaction transaction : transactions)
		{
			builder.writeLine(transaction.getTimestamp().toLocaleString());
			builder.writeLine(transaction.getDelta() + " po");
			builder.writeLine(transaction.getLog());
			builder.writeLine("-----");

			if ((++i % transactionsPerPage) == 0)
				builder.turnPage();
		}

		return builder.build();
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event)
	{
		String line = event.getLine(1);

		if (line.equalsIgnoreCase(CONSULT))
		{
			event.setLine(1, BLUE_CONSULT);
			return true;
		}
		else if (line.equalsIgnoreCase(DEPOSIT))
		{
			event.setLine(1, BLUE_DEPOSIT);
			return true;
		}
		else if (line.equalsIgnoreCase(WITHDRAW))
		{
			event.setLine(1, BLUE_WITHDRAW);
			return true;
		}
		else if (line.equalsIgnoreCase(STATEMENT))
		{
			event.setLine(1, BLUE_STATEMENT);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		String line = sign.getLine(1);

		if (line.equals(BLUE_CONSULT))
		{
			onConsultSignClick(player);
		}
		else if (line.equals(BLUE_DEPOSIT))
		{
			onDepositSignClick(player);
		}
		else if (line.equals(BLUE_WITHDRAW))
		{
			onWithdrawSignClick(player);
		}
		else if (line.equals(BLUE_STATEMENT))
		{
			onStatementSignClick(player);
		}
	}

	protected abstract void onConsultSignClick(Player player) throws HeavenException;

	protected abstract void onDepositSignClick(Player player) throws HeavenException;

	protected abstract void onWithdrawSignClick(Player player) throws HeavenException;

	protected abstract void onStatementSignClick(Player player) throws HeavenException;
}
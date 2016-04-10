package fr.heavencraft.heavenrp.database.transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccount;

public class TransactionProvider
{
	private static final String LAST_TRANSACTIONS = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC LIMIT 98;";

	public static Collection<Transaction> getLastTransactions(BankAccount account) throws HeavenException
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(LAST_TRANSACTIONS))
		{
			ps.setInt(1, account.getId());

			final ResultSet rs = ps.executeQuery();

			// Use a list to keep the order of transactions
			final Collection<Transaction> result = new ArrayList<Transaction>();
			while (rs.next())
			{
				result.add(new Transaction(rs));
			}
			return result;
		}
		catch (final SQLException e)
		{
			e.printStackTrace();
			throw new SQLErrorException();
		}
	}
}
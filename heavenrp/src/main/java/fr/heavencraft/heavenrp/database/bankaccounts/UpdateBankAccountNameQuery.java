package fr.heavencraft.heavenrp.database.bankaccounts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class UpdateBankAccountNameQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE bank_account SET owner = ? WHERE id = ? LIMIT 1;";

	private final BankAccount bankAccount;
	private final String name;

	public UpdateBankAccountNameQuery(BankAccount bankAccount, String name)
	{
		this.bankAccount = bankAccount;
		this.name = name;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (!Bukkit.isPrimaryThread())
			throw new HeavenException("UpdateBankAccountNameQuery should not be executed asynchronously.");

		if (name.equals(bankAccount.getName()))
			return; // Nothing to do

		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, name);
			ps.setInt(2, bankAccount.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();
		}
	}
}
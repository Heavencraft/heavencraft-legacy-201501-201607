package fr.heavencraft.heavenrp.database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;

public class UserProvider
{
	private static final String SELECT_USER_BY_UUID = "SELECT * FROM users WHERE uuid = ? LIMIT 1;";
	private static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ? LIMIT 1;";

	public static User getUserByUUID(String uuid) throws HeavenException
	{
		// Try to get user from cache
		User user = UsersCache.getUserByUUID(uuid);
		if (user != null)
			return user;

		// Get user from database
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_UUID))
		{
			ps.setString(1, uuid);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new UserNotFoundException(uuid);

			user = new User(rs);
			UsersCache.addToCache(user);
			return user;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static User getUserByName(String name) throws HeavenException
	{
		// Try to get user from cache
		User user = UsersCache.getUserByName(name);
		if (user != null)
			return user;

		// Get user from database
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new UserNotFoundException(name);

			user = new User(rs);
			UsersCache.addToCache(user);
			return user;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public static void createUser(String uuid, String name)
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement("INSERT INTO users (uuid, name) VALUES (?, ?);"))
		{
			ps.setString(1, uuid);
			ps.setString(2, name);

			ps.executeUpdate();
			ps.close();

			BankAccountsManager.createBankAccount(name, BankAccountType.USER);
		}

		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (final HeavenException ex)
		{
		}
	}

}
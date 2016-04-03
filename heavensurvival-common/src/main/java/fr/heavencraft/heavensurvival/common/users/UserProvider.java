package fr.heavencraft.heavensurvival.common.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavensurvival.common.HeavenSurvivalInstance;

public class UserProvider
{
	private static final String SELECT_USER_BY_UUID = "SELECT * FROM users WHERE uuid = ? LIMIT 1;";
	private static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ? LIMIT 1;";

	private static UserProvider instance;

	public static UserProvider getInstance()
	{
		if (instance == null)
			instance = new UserProvider(HeavenSurvivalInstance.get().getConnectionProvider());

		return instance;
	}

	private final ConnectionProvider connectionProvider;
	private final UsersCache cache = new UsersCache();

	private UserProvider(ConnectionProvider connectionProvider)
	{
		this.connectionProvider = connectionProvider;
	}

	public User getUserByUniqueId(UUID uniqueId) throws HeavenException
	{
		// Try to get user from cache
		User user = cache.getUserByUniqueId(uniqueId);
		if (user != null)
			return user;

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_UUID))
		{
			ps.setString(1, uniqueId.toString());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new UserNotFoundException(uniqueId);

			user = new User(rs);
			cache.addToCache(user);
			return user;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public User getUserByName(String name) throws HeavenException
	{
		// Try to get user from cache
		User user = cache.getUserByName(name);
		if (user != null)
			return user;

		// Get user from database
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_USER_BY_NAME))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new UserNotFoundException(name);

			user = new User(rs);
			cache.addToCache(user);
			return user;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public void createUser(UUID uniqueId, String name)
	{
		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement("INSERT INTO users (uuid, name) VALUES (?, ?);"))
		{
			ps.setString(1, uniqueId.toString());
			ps.setString(2, name);

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	public void invalidateCache(User user)
	{
		cache.invalidateCache(user);
	}
}
package fr.heavencraft.heavencore.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.sql.ConnectionProvider;

public abstract class UserProvider<U extends User>
{
	private static final String SELECT_USER_BY_UUID = "SELECT * FROM users WHERE uuid = ? LIMIT 1;";
	private static final String SELECT_USER_BY_NAME = "SELECT * FROM users WHERE name = ? LIMIT 1;";

	private final ConnectionProvider connectionProvider;
	private final UsersCache<U> cache = new UsersCache<U>();
	private final UserFactory<U> factory;

	protected UserProvider(ConnectionProvider connectionProvider, UserFactory<U> factory)
	{
		this.connectionProvider = connectionProvider;
		this.factory = factory;
	}

	public U getUserByUniqueId(UUID uniqueId) throws HeavenException
	{
		// Try to get user from cache
		U user = cache.getUserByUniqueId(uniqueId);
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

			user = factory.newUser(rs);
			cache.addToCache(user);
			return user;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public U getUserByName(String name) throws HeavenException
	{
		// Try to get user from cache
		U user = cache.getUserByName(name);
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

			user = factory.newUser(rs);
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

	public Collection<U> getAllUsers()
	{
		return null;
	}

	public ConnectionProvider getConnectionProvider()
	{
		return connectionProvider;
	}

	public void invalidateCache(User user)
	{
		cache.invalidateCache(user);
	}
}
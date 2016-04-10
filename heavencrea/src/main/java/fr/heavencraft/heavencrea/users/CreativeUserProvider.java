package fr.heavencraft.heavencrea.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import fr.heavencraft.deprecated.DeprecatedUserProvider;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavencore.sql.ConnectionProvider;

public class CreativeUserProvider implements DeprecatedUserProvider<CreativeUser>
{
	// SQL Queries
	private static final String PRELOAD_USERS = "SELECT * FROM users;";
	private static final String LOAD_USER = "SELECT * FROM users WHERE uuid = ? LIMIT 1;";
	private static final String CREATE_USERS = "INSERT INTO users (uuid, name) VALUES (?, ?);";

	// Logger
	private static final HeavenLog log = HeavenLog.getLogger(CreativeUserProvider.class);

	private final ConnectionProvider connectionHandler;
	private final Map<UUID, CreativeUser> usersByUniqueId = new HashMap<UUID, CreativeUser>();
	private final Map<String, CreativeUser> usersByName = new HashMap<String, CreativeUser>();

	public CreativeUserProvider(ConnectionProvider connectionHandler)
	{
		this.connectionHandler = connectionHandler;

		loadUsers();
	}

	/*
	 * Load user(s) from database
	 */

	private void loadUsers()
	{
		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(PRELOAD_USERS))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				int count = 0;

				while (rs.next())
				{
					++count;
					addToCache(new CreativeUser(connectionHandler, rs));
				}

				log.info("%1$s users loaded from database.", count);
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown(); // Close server if we can't load users
		}
	}

	private CreativeUser loadUser(UUID uuid) throws UserNotFoundException, SQLErrorException
	{
		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(LOAD_USER))
		{
			ps.setString(1, uuid.toString());

			try (ResultSet rs = ps.executeQuery())
			{
				if (!rs.next())
					throw new UserNotFoundException(uuid);

				final CreativeUser user = new CreativeUser(connectionHandler, rs);
				addToCache(user);
				return user;
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	/*
	 * Cache management
	 */

	private void addToCache(CreativeUser user)
	{
		// Add to "UUID -> User" cache
		usersByUniqueId.put(user.getUniqueId(), user);

		// Add to "Name -> User" cache
		usersByName.put(user.getName(), user);
	}

	@Override
	public void clearCache()
	{
		// Clear cache
		usersByName.clear();

		// Reload users from database
		loadUsers();
	}

	@Override
	public CreativeUser createUser(UUID uuid, String name) throws HeavenException
	{
		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(CREATE_USERS))
		{
			ps.setString(1, uuid.toString());
			ps.setString(2, name);

			if (ps.executeUpdate() != 1)
			{
				throw new HeavenException("La region existe déjà");
			}

			return loadUser(uuid);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	@Override
	public CreativeUser getUserByUniqueId(UUID uuid) throws UserNotFoundException
	{
		final CreativeUser user = usersByUniqueId.get(uuid);

		if (user == null)
			throw new UserNotFoundException(uuid);

		return user;
	}

	@Override
	public CreativeUser getUserByName(String name) throws UserNotFoundException
	{
		final CreativeUser user = usersByName.get(name);

		if (user == null)
			throw new UserNotFoundException(name);

		return user;
	}

	@Override
	public Collection<CreativeUser> getAllUsers()
	{
		return usersByUniqueId.values();
	}
}
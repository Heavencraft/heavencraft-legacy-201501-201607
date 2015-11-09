package fr.heavencraft.heavenrp.database.users;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class UpdateUserNameQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET name = ? WHERE id = ? LIMIT 1;";

	private final User user;
	private final String name;

	public UpdateUserNameQuery(User user, String name)
	{
		this.user = user;
		this.name = name;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (!Bukkit.isPrimaryThread())
			throw new HeavenException("UpdateUserNameQuery should not be executed asynchronously.");

		if (name.equals(user.getName()))
			return; // Nothing to do

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(QUERY))
		{
			ps.setString(1, name);
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UsersCache.invalidateCache(user);
		}
	}
}
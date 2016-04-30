package fr.heavencraft.heavencore.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class UpdateUserNameQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET name = ? WHERE uuid = ? LIMIT 1;";

	private final User user;
	private final String name;
	private final UserProvider<? extends User> userProvider;

	public UpdateUserNameQuery(User user, String name, UserProvider<? extends User> userProvider)
	{
		this.user = user;
		this.name = name;
		this.userProvider = userProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (name.equals(user.getName()))
			return; // Nothing to do

		try (Connection connection = userProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, name);
			ps.setString(2, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			userProvider.invalidateCache(user);
		}
	}
}
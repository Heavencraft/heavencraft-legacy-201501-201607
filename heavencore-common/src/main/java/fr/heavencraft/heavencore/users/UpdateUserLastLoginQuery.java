package fr.heavencraft.heavencore.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class UpdateUserLastLoginQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET last_login = ? WHERE uuid = ? LIMIT 1;";

	private final User user;
	private final UserProvider<? extends User> userProvider;

	public UpdateUserLastLoginQuery(User user, UserProvider<? extends User> userProvider)
	{
		this.user = user;
		this.userProvider = userProvider;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = userProvider.getConnectionProvider().getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setTimestamp(1, new Timestamp(new Date().getTime()));
			ps.setString(2, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			userProvider.invalidateCache(user);
		}
	}
}
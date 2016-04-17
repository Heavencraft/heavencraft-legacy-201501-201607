package fr.heavencraft.heavensurvival.common.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavensurvival.common.HeavenSurvivalInstance;

public class UpdateUserNameQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET name = ? WHERE uuid = ? LIMIT 1;";

	private final ConnectionProvider connectionProvider = HeavenSurvivalInstance.get().getConnectionProvider();
	private final SurvivalUser user;
	private final String name;

	public UpdateUserNameQuery(SurvivalUser user, String name)
	{
		this.user = user;
		this.name = name;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (name.equals(user.getName()))
			return; // Nothing to do

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setString(1, name);
			ps.setString(2, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			SurvivalUserProvider.get().invalidateCache(user);
		}
	}
}
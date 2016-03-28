package fr.heavencraft.heavensurvival.common.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavensurvival.common.HeavenSurvivalInstance;

public class UpdateUserPvpQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET pvp = ? WHERE uuid = ? LIMIT 1;";

	private final ConnectionProvider connectionProvider = HeavenSurvivalInstance.get().getConnectionProvider();
	private final User user;
	private final boolean pvp;

	public UpdateUserPvpQuery(User user, boolean pvp)
	{
		this.user = user;
		this.pvp = pvp;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (pvp == user.isPvp())
			return; // Nothing to do

		try (Connection connection = connectionProvider.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setBoolean(1, pvp);
			ps.setString(2, user.getUniqueIdAsString());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UserProvider.getInstance().invalidateCache(user);
		}
	}
}
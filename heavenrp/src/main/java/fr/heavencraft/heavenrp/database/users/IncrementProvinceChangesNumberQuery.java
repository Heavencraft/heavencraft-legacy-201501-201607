package fr.heavencraft.heavenrp.database.users;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class IncrementProvinceChangesNumberQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET province_changes = province_changes + 1 WHERE id = ? LIMIT 1";

	private final User user;

	public IncrementProvinceChangesNumberQuery(User user)
	{
		this.user = user;
	}
	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(QUERY))
		{
			ps.setInt(1, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UsersCache.invalidateCache(user);
		}
		
	}

}

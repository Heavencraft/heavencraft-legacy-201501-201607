package fr.heavencraft.heavenrp.database.provinces;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;

public class RemoveProvinceQuery extends AbstractQuery
{
	private static final String QUERY = "DELETE FROM mayor_people WHERE user_id = ?";

	private final User user;

	public RemoveProvinceQuery(User user)
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
		}
	}
}
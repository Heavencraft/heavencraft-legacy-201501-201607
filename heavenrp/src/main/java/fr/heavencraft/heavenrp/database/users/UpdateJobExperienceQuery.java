package fr.heavencraft.heavenrp.database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class UpdateJobExperienceQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET job_experience = job_experience + ? WHERE id = ? AND job_experience + ? >= 0 LIMIT 1;";

	private final User user;
	private final int delta;

	public UpdateJobExperienceQuery(User user, int delta)
	{
		this.user = user;
		this.delta = delta;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		if (delta == 0)
			return; // Nothing to do

		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, delta);
			ps.setInt(2, user.getId());
			ps.setInt(3, delta);

			System.out.println("Executing query " + ps);
			if (ps.executeUpdate() == 0)
				throw new HeavenException("Vous fouillez dans votre bourse... Vous n'avez pas assez.");

			UsersCache.invalidateCache(user);
		}
	}
}
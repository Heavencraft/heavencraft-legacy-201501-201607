package fr.heavencraft.heavenrp.database.users;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.jobs.Job;

public class SetJobQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET job_id = ?, job_experience = 0 WHERE id = ? LIMIT 1;";

	private final User user;
	private final int job;

	public SetJobQuery(User user, Job job)
	{
		this.user = user;
		this.job = job.getId();
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, job);
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UsersCache.invalidateCache(user);
		}
	}
}
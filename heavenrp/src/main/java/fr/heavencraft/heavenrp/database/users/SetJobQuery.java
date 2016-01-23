package fr.heavencraft.heavenrp.database.users;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.jobs.Job;

public class SetJobQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET job_name = ?, job_experience = 0 WHERE id = ? LIMIT 1;";

	private final User user;
	private final String jobName;

	public SetJobQuery(User user, Job job)
	{
		this.user = user;
		this.jobName = job.getDisplayName();
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(QUERY))
		{
			ps.setString(1, jobName);
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UsersCache.invalidateCache(user);
		}
	}
}
package fr.heavencraft.heavenrp.database.users;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class UpdateUserDealerLicenceQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE users SET dealer_license = ? WHERE id = ? LIMIT 1;";

	private final User user;

	public UpdateUserDealerLicenceQuery(User user)
	{
		this.user = user;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		final Calendar newDate = Calendar.getInstance();

		if (user.hasDealerLicense())
			newDate.setTime(user.getLicenseExpireDate());

		newDate.add(Calendar.MONTH, 1);

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(QUERY))
		{
			ps.setTimestamp(1, new Timestamp(newDate.getTimeInMillis()));
			ps.setInt(2, user.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();

			UsersCache.invalidateCache(user);
		}
	}
}
package fr.heavencraft.heavenrp.database.provinces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;

public class UpdateProvinceQuery extends AbstractQuery
{
	private static final String QUERY = "INSERT INTO mayor_people (user_id, city_id) VALUES (?, ?)";

	private final User user;
	private final Province province;

	public UpdateProvinceQuery(User user, Province province)
	{
		this.user = user;
		this.province = province;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, user.getId());
			ps.setInt(2, province.getId());

			System.out.println("Executing query " + ps);
			ps.executeUpdate();
		}
	}
}
package fr.heavencraft.heavenrp.database.homes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Location;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;

public class SetHomeQuery extends AbstractQuery
{
	private static final String QUERY = "REPLACE INTO homes SET user_id = ?, home_nb = ?, world = ?, x = ?, y = ?, z = ?, pitch = ?, yaw = ?";

	private final User user;
	private final int homeNumber;
	private final String world;
	private final double x;
	private final double y;
	private final double z;
	private final float pitch;
	private final float yaw;

	public SetHomeQuery(User user, int homeNumber, Location location)
	{
		this.user = user;
		this.homeNumber = homeNumber;
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.pitch = location.getPitch();
		this.yaw = location.getYaw();
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = HeavenRP.getConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, user.getId());
			ps.setInt(2, homeNumber);
			ps.setString(3, world);
			ps.setDouble(4, x);
			ps.setDouble(5, y);
			ps.setDouble(6, z);
			ps.setFloat(7, pitch);
			ps.setFloat(8, yaw);

			System.out.println("Executing query " + ps);

			ps.executeUpdate();

			// Clear the cache
			HomeCache.invalidateCache(user, homeNumber);
		}
	}
}
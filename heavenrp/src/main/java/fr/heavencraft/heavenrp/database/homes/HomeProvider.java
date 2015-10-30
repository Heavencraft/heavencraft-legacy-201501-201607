package fr.heavencraft.heavenrp.database.homes;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;

public class HomeProvider
{
	public static Location getHome(User user, int nb) throws HeavenException
	{
		if (nb < 1 || nb > user.getHomeNumber())
			throw new HeavenException("Vous n'avez pas acheté le {home %1$d}.", nb);

		// Try to get it from cache
		Location home = HomeCache.getHome(user, nb);
		if (home != null)
			return home;

		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT world, x, y, z, yaw, pitch FROM homes WHERE user_id = ? AND home_nb = ? LIMIT 1"))
		{
			ps.setInt(1, user.getId());
			ps.setInt(2, nb);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new HeavenException("Vous n'avez pas configuré votre {home %1$d}.", nb);

			home = new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"),
					rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
			HomeCache.addToCache(user, nb, home);
			return home;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Cette erreur n'est pas sensée se produire.");
		}
	}
}
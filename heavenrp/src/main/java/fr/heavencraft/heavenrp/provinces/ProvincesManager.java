package fr.heavencraft.heavenrp.provinces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.exceptions.ProvinceNotFoundException;

public class ProvincesManager
{
	public static class Province
	{
		private final int _id;
		private final String _login;
		private final String _color;
		private final Location _warp;

		private Province(ResultSet rs) throws SQLException
		{
			_id = rs.getInt("id");
			_login = rs.getString("login");
			_color = "§" + rs.getString("color");

			final String world = rs.getString("world");
			final double x = rs.getDouble("x");
			final double y = rs.getDouble("y");
			final double z = rs.getDouble("z");
			final float yaw = rs.getFloat("yaw");
			final float pitch = rs.getFloat("pitch");

			_warp = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		}

		private Province(int id, String login, String color, String world, double x, double y, double z,
				float yaw, float pitch)
		{
			_id = id;
			_login = login;
			_color = color;
			_warp = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		}

		public int getId()
		{
			return _id;
		}

		public String getName()
		{
			return _login;
		}

		public String getColor()
		{
			return _color;
		}

		public Location getWarp()
		{
			return _warp;
		}

		/*
		 * public void setWarp(Location warp) { _warp = warp;
		 * 
		 * update(); }
		 */

		/*
		 * private void update() { try { PreparedStatement ps =
		 * HeavenRP.getConnection().prepareStatement(
		 * "UPDATE mayor_cities SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ? WHERE id = ?"
		 * ); ps.setString(1, _warp.getWorld().getName()); ps.setDouble(2,
		 * _warp.getX()); ps.setDouble(3, _warp.getY()); ps.setDouble(4,
		 * _warp.getZ()); ps.setFloat(5, _warp.getYaw()); ps.setFloat(6,
		 * _warp.getPitch()); ps.setInt(7, _id);
		 * 
		 * ps.executeUpdate(); } catch (SQLException ex) { ex.printStackTrace();
		 * } }
		 */
	}

	public static Province getProvinceById(int id) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT * FROM mayor_cities WHERE id = ? LIMIT 1"))
		{
			ps.setInt(1, id);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new ProvinceNotFoundException(id);

			return new Province(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new ProvinceNotFoundException(id);
		}
	}

	public static Province getProvinceByName(String name) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"SELECT * FROM mayor_cities WHERE login = ? LIMIT 1"))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new ProvinceNotFoundException(name);

			return new Province(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new ProvinceNotFoundException(name);
		}
	}

	public static Province getProvinceByUser(User user)
	{
		try (PreparedStatement ps = HeavenRP
				.getConnection()
				.prepareStatement(
						"SELECT mc.id, mc.login, mc.color, mc.world, mc.x, mc.y, mc.z, mc.yaw, mc.pitch FROM mayor_cities mc, mayor_people mp WHERE mc.id = mp.city_id AND mp.user_id = ? LIMIT 1"))
		{
			ps.setInt(1, user.getId());

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				return null;

			return new Province(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
}

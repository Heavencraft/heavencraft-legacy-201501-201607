package fr.heavencraft.heavenrp.warps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.exceptions.UnknownErrorException;
import fr.heavencraft.heavenrp.exceptions.WarpNotFoundException;

public class WarpsManager
{
	public static class Warp
	{
		private final String _name;
		private final Location _location;

		private Warp(ResultSet rs) throws SQLException
		{
			final World world = Bukkit.getWorld(rs.getString("world"));
			final double x = rs.getDouble("x");
			final double y = rs.getDouble("y");
			final double z = rs.getDouble("z");
			final float yaw = rs.getFloat("yaw");
			final float pitch = rs.getFloat("pitch");

			_name = rs.getString("name");
			_location = new Location(world, x, y, z, yaw, pitch);
		}

		public Location getLocation()
		{
			return _location;
		}

		public void remove() throws HeavenException
		{
			try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("DELETE FROM warps WHERE name = ? LIMIT 1"))
			{
				ps.setString(1, _name);

				ps.executeUpdate();
			}
			catch (final SQLException ex)
			{
				ex.printStackTrace();
				throw new UnknownErrorException();
			}
		}
	}

	public static Warp getWarp(String name) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("SELECT * FROM warps WHERE name = ? LIMIT 1"))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new WarpNotFoundException(name);

			return new Warp(rs);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new WarpNotFoundException(name);
		}
	}

	public static List<String> listWarps() throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("SELECT name FROM warps"))
		{
			final List<String> warps = new ArrayList<String>();

			final ResultSet rs = ps.executeQuery();

			while (rs.next())
				warps.add(rs.getString("name"));

			return warps;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new UnknownErrorException();
		}
	}

	public static void createWarp(String name, Location location) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement(
				"REPLACE INTO warps SET name = ?, world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ?"))
		{
			ps.setString(1, name);
			ps.setString(2, location.getWorld().getName());
			ps.setDouble(3, location.getX());
			ps.setDouble(4, location.getY());
			ps.setDouble(5, location.getZ());
			ps.setFloat(6, location.getYaw());
			ps.setFloat(7, location.getPitch());

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Cette erreur n'est pas sensée se produire.");
		}
	}
}
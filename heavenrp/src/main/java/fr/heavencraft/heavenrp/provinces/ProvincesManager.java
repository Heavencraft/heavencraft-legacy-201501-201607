package fr.heavencraft.heavenrp.provinces;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;

import fr.heavencraft.heavencore.exceptions.HeavenException;
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
		private final int _points;

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
			final int points = rs.getInt("points");

			_warp = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			_points = points;
		}

		private Province(int id, String login, String color, String world, double x, double y, double z,
				float yaw, float pitch, int points)
		{
			_id = id;
			_login = login;
			_color = color;
			_warp = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			_points = points;
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
		
		public int getPoints()
		{
			return _points;
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
	
	public static List<Province> getProvinces() {
		List<Province> provinces = new ArrayList<Province>();
		
		try (PreparedStatement ps = HeavenRP.getConnection().prepareStatement("SELECT * FROM mayor_cities"))
		{
			final ResultSet rs = ps.executeQuery();
			Bukkit.broadcastMessage(rs.getRow() + "   ");
			while(rs.next())
				provinces.add(new Province(rs));
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			return null;
		}
		return provinces;
	}
	
	
	/**
	 * Returns a level by points
	 * 
	 * @param points
	 * @return level (positive value)
	 */
	public static int getLevel(int points)
	{
		if(points >= 2000)
			return 16;
		if(points >= 1800)
			return 15;
		if(points >= 1600)
			return 14;
		if(points >= 1400)
			return 13;
		if(points >= 1200)
			return 12;
		if(points >= 1000)
			return 11;
		if(points >= 850)
			return 10;
		if(points >= 750)
			return 9;
		if(points >= 650)
			return 8;
		if(points >= 500)
			return 7;
		if(points >= 400)
			return 6;
		if(points >= 300)
			return 5;
		if(points >= 200)
			return 4;
		if(points >= 150)
			return 3;
		if(points >= 100)
			return 2;
		if(points >= 50)
			return 1;
		return 0;
	}
	
	/**
	 * Returns an effect type depending on the /effect ID
	 * @param effectId
	 * @return
	 */
	public static PotionEffectType getEffectType(int effectId) {
		if(effectId == 1)
			return PotionEffectType.SPEED;
		if(effectId == 2)
			return PotionEffectType.SLOW;
		if(effectId == 3)
			return PotionEffectType.FAST_DIGGING;
		if(effectId == 4)
			return PotionEffectType.SLOW_DIGGING;
		if(effectId == 5)
			return PotionEffectType.INCREASE_DAMAGE;
		if(effectId == 6)
			return PotionEffectType.HEAL;
		if(effectId == 7)
			return PotionEffectType.HARM;
		if(effectId == 8)
			return PotionEffectType.JUMP;
		if(effectId == 9)
			return PotionEffectType.CONFUSION;
		if(effectId == 10)
			return PotionEffectType.REGENERATION;
		if(effectId == 11)
			return PotionEffectType.DAMAGE_RESISTANCE;
		if(effectId == 12)
			return PotionEffectType.FIRE_RESISTANCE;
		if(effectId == 13)
			return PotionEffectType.WATER_BREATHING;
		if(effectId == 14)
			return PotionEffectType.INVISIBILITY;
		if(effectId == 15)
			return PotionEffectType.BLINDNESS;
		if(effectId == 16)
			return PotionEffectType.NIGHT_VISION;
		if(effectId == 17)
			return PotionEffectType.HUNGER;
		if(effectId == 18)
			return PotionEffectType.WEAKNESS;
		if(effectId == 19)
			return PotionEffectType.POISON;
		if(effectId == 20)
			return PotionEffectType.WITHER;
		if(effectId == 21)
			return PotionEffectType.HEALTH_BOOST;
		if(effectId == 22)
			return PotionEffectType.ABSORPTION;
		if(effectId == 23)
			return PotionEffectType.SATURATION;
		return null;
	}
}

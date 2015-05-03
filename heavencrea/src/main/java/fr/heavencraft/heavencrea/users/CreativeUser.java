package fr.heavencraft.heavencrea.users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavencore.users.User;
import fr.heavencraft.heavencore.users.color.TabColor;

public class CreativeUser implements User
{
	// SQL Queries
	private static final String UPDATE_LASTLOGIN = "UPDATE users SET last_login = ? WHERE uuid = ? LIMIT 1;";
	private static final String UPDATE_BALANCE = "UPDATE users SET balance = ? WHERE uuid = ? LIMIT 1;";
	private static final String UPDATE_TAB_COLOR = "UPDATE users SET tab_color = ? WHERE uuid = ? LIMIT 1;";

	private static final String GET_HOME = "SELECT world, x, y, z, yaw, pitch FROM homes WHERE user_id = ? AND home_nb = ? LIMIT 1";
	private static final String SET_HOME = "REPLACE INTO homes SET world = ?, x = ?, y = ?, z = ?, yaw = ?, pitch = ?, user_id = ?, home_nb = ?";
	private static final String UPDATE_NAME = "UPDATE users SET name = ? WHERE uuid = ? LIMIT 1";
	private static final String INCREMENT_HOME_NUMBER = "UPDATE users SET home_number = home_number + 1 WHERE id = ? LIMIT 1";

	private final ConnectionHandler connectionHandler;

	private final int id;
	private final String uuid;
	private final String name;
	private TabColor tabColor;
	private int balance;
	private int homeNumber;
	private Timestamp lastLogin;

	public CreativeUser(ConnectionHandler connectionHandler, ResultSet rs) throws SQLException
	{
		this.connectionHandler = connectionHandler;

		id = rs.getInt("id");
		uuid = rs.getString("uuid");
		name = rs.getString("name");
		tabColor = TabColor.getUniqueInstanceByCode(rs.getString("tab_color"));

		balance = rs.getInt("balance");
		homeNumber = rs.getInt("home_number");
		lastLogin = rs.getTimestamp("last_login");
	}

	@Override
	public UUID getUniqueId()
	{
		return UUID.fromString(uuid);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void updateName(String name) throws HeavenException
	{
		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(UPDATE_NAME))
		{
			ps.setString(1, name);
			ps.setString(2, uuid);

			if (ps.executeUpdate() != 1)
				throw new UserNotFoundException(name);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	/*
	 * Balance
	 */

	public int getBalance()
	{
		return balance;
	}

	public void updateBalance(int delta) throws HeavenException
	{
		if (balance < 0)
			throw new HeavenException(
					"Vous avez moins de 0 jetons sur vous. Merci de contacter un administrateur.");

		if (balance + delta < 0)
			throw new HeavenException("Vous n'avez pas assez de jetons.");

		balance += delta;

		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(UPDATE_BALANCE))
		{
			ps.setInt(1, balance);
			ps.setString(2, uuid);
			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	/*
	 * Homes
	 */

	public int getHomeNumber()
	{
		return homeNumber;
	}

	public void incrementHomeNumber() throws HeavenException
	{
		try
		{
			final PreparedStatement ps = connectionHandler.getConnection()
					.prepareStatement(INCREMENT_HOME_NUMBER);
			ps.setInt(1, id);
			ps.executeUpdate();

			homeNumber++;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public Location getHome(int nb) throws HeavenException
	{
		if (nb < 1 || nb > homeNumber)
			throw new HeavenException("Vous n'avez pas acheté le {home %1$d}.", nb);

		try
		{
			final PreparedStatement ps = connectionHandler.getConnection().prepareStatement(GET_HOME);
			ps.setInt(1, id);
			ps.setInt(2, nb);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new HeavenException("Vous n'avez pas configuré votre {home %1$d}.", nb);

			return new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"),
					rs.getDouble("z"), rs.getFloat("yaw"), rs.getFloat("pitch"));
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	public void setHome(int nb, Location home) throws HeavenException
	{
		if (nb < 1 || nb > homeNumber)
			throw new HeavenException("Vous n'avez pas acheté le {home %1$d}.", nb);

		try
		{
			final PreparedStatement ps = connectionHandler.getConnection().prepareStatement(SET_HOME);
			ps.setString(1, home.getWorld().getName());
			ps.setDouble(2, home.getX());
			ps.setDouble(3, home.getY());
			ps.setDouble(4, home.getZ());
			ps.setFloat(5, home.getYaw());
			ps.setFloat(6, home.getPitch());

			ps.setInt(7, id);
			ps.setInt(8, nb);

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	/*
	 * Last login
	 */

	@Override
	public Date getLastLogin()
	{
		return lastLogin;
	}

	@Override
	public void updateLastLogin(Date date) throws SQLErrorException
	{
		final Timestamp lastLogin = new Timestamp(date.getTime());

		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(UPDATE_LASTLOGIN))
		{
			ps.setTimestamp(1, lastLogin);
			ps.setString(2, uuid);
			ps.executeUpdate();

			this.lastLogin = lastLogin;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}

	@Override
	public TabColor getTabColor()
	{
		return tabColor;
	}

	public void setTabColor(TabColor tabColor) throws HeavenException
	{
		try (PreparedStatement ps = connectionHandler.getConnection().prepareStatement(UPDATE_TAB_COLOR))
		{
			ps.setString(1, tabColor.getCode());
			ps.setString(2, uuid);
			ps.executeUpdate();

			this.tabColor = tabColor;
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new SQLErrorException();
		}
	}
}
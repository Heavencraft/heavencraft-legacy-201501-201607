package fr.heavencraft.heavencrea.hps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.sql.ConnectionHandler;

public class HpsManager
{
	public static final int TAUX_JETON = 80;
	private final ConnectionHandler connectionHandler;

	public HpsManager(ConnectionHandler connectionHandler)
	{
		this.connectionHandler = connectionHandler;
	}

	// 1 Hps <-> 750 Jeton
	public void removeBalance(String name, int hps) throws HeavenException
	{
		if (getBalance(name) < hps)
			throw new HeavenException("Vous n'avez pas assez d'argent sur votre compte.");

		try
		{
			final PreparedStatement ps = connectionHandler.getConnection().prepareStatement(
					"UPDATE heavencraft_users SET balance = balance - ? WHERE username = ?");
			ps.setInt(1, hps);
			ps.setString(2, name);

			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Erreur SQL. Nous sommes désolé.");
		}
	}

	public int getBalance(String name) throws HeavenException
	{
		try
		{
			final PreparedStatement ps = connectionHandler.getConnection().prepareStatement(
					"SELECT balance FROM heavencraft_users WHERE username = ?");
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
				throw new HeavenException("Vous n'avez pas de compte sur le site.");

			return rs.getInt("balance");
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			throw new HeavenException("Vous n'avez pas assez d'argent sur mon compte.");
		}
	}
}
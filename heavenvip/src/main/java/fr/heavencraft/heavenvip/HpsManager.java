package fr.heavencraft.heavenvip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class HpsManager
{

	public static int getBalance(String name) throws HeavenException
	{
		try (Connection connection = HeavenVIP.getWebConnection().getConnection();
				PreparedStatement ps = connection
						.prepareStatement("SELECT balance FROM heavencraft_users WHERE username = ?"))
		{
			ps.setString(1, name);

			final ResultSet rs = ps.executeQuery();

			if (!rs.next())
			{
				throw new HeavenException("Vous n'avez pas de compte sur le site.");
			}
			return rs.getInt("balance");
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
		throw new HeavenException("Erreur inconnue!");
	}
}
package fr.heavencraft.heavenrp.hps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.exceptions.UnknownErrorException;

public class HpsManager
{

	public static int getBalance(String name) throws HeavenException
	{
		try (PreparedStatement ps = HeavenRP.getMainConnection().prepareStatement(
				"SELECT balance FROM heavencraft_users WHERE username = ?"))
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
		throw new UnknownErrorException();
	}
}
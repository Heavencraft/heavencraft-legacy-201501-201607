package fr.heavencraft.heavenrp.database.hps;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class UpdateHPSBalanceQuery extends AbstractQuery
{
	private static final String QUERY = "UPDATE heavencraft_users SET balance = balance + ? WHERE username = ? AND balance + ? >= 0 LIMIT 1";

	private final String name;
	private final int delta;

	public UpdateHPSBalanceQuery(String name, int delta)
	{
		this.name = name;
		this.delta = delta;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (Connection connection = HeavenRP.getMainConnection();
				PreparedStatement ps = connection.prepareStatement(QUERY))
		{
			ps.setInt(1, delta);
			ps.setString(2, name);
			ps.setInt(3, delta);

			if (ps.executeUpdate() == 0)
				throw new HeavenException("Vous n'avez pas assez d'argent sur mon compte.");
		}
	}
}
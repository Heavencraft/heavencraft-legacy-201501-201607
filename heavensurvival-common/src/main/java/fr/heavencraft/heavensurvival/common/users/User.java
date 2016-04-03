package fr.heavencraft.heavensurvival.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class User
{
	private final String uniqueIdAsString;

	private final UUID uniqueId;
	private final String name;
	private final boolean pvp;

	User(ResultSet rs) throws SQLException
	{
		uniqueIdAsString = rs.getString("uuid");

		uniqueId = UUID.fromString(uniqueIdAsString);
		name = rs.getString("name");
		pvp = rs.getBoolean("pvp");
	}

	@Override
	public String toString()
	{
		return name;
	}

	public String getUniqueIdAsString()
	{
		return uniqueIdAsString;
	}

	public UUID getUniqueId()
	{
		return uniqueId;
	}

	public String getName()
	{
		return name;
	}

	public boolean isPvp()
	{
		return pvp;
	}
}
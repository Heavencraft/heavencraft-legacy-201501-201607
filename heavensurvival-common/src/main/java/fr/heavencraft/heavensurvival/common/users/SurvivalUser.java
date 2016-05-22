package fr.heavencraft.heavensurvival.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import fr.heavencraft.heavencore.users.color.TabColor;
import fr.heavencraft.heavencore.users.color.UserWithColor;

public class SurvivalUser implements UserWithColor
{
	private final String uniqueIdAsString;

	private final UUID uniqueId;
	private final String name;
	private final boolean pvp;

	SurvivalUser(ResultSet rs) throws SQLException
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

	@Override
	public String getUniqueIdAsString()
	{
		return uniqueIdAsString;
	}

	@Override
	public UUID getUniqueId()
	{
		return uniqueId;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public boolean isPvp()
	{
		return pvp;
	}

	@Override
	public TabColor getTabColor()
	{
		return isPvp() ? TabColor.RED : TabColor.WHITE;
	}

	@Override
	public Timestamp getLastLogin()
	{
		return null;
	}
}
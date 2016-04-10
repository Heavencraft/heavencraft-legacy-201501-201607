package fr.heavencraft.heavensurvival.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import fr.heavencraft.heavencore.users.User;
import fr.heavencraft.heavencore.users.color.TabColor;

public class SurvivalUser implements User
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

	public TabColor getTabColor()
	{
		return isPvp() ? TabColor.RED : TabColor.WHITE;
	}

	public Date getLastLogin()
	{
		return null;
	}
}
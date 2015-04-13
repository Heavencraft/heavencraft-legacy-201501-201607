package fr.heavencraft.heavenguard.datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.sql.ConnectionHandler;
import fr.heavencraft.heavenguard.api.FlagHandler;
import fr.heavencraft.heavenguard.api.GlobalRegion;

public class SQLGlobalRegion implements GlobalRegion
{
	private final String name;

	private final FlagHandler flagHandler;

	public SQLGlobalRegion(ConnectionHandler connectionHandler, ResultSet rs) throws SQLException
	{
		name = rs.getString("name");

		// Load flags
		flagHandler = new SQLFlagHandler(connectionHandler, rs, null);
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public FlagHandler getFlagHandler()
	{
		return flagHandler;
	}

	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		str.append(name);
		str.append(" [");
		str.append(flagHandler);
		str.append("]");
		return str.toString();
	}
}
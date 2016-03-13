package fr.heavencraft.heavencore.sql;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class ConnectionHandlerFactory
{
	public static ConnectionProvider getConnectionHandler(Database database)
	{
		return new DefaultConnectionHandler(database);
	}

	public static ConnectionProvider getConnectionHandler(String database) throws HeavenException
	{
		return getConnectionHandler(Database.getUniqueInstanceByName(database));
	}
}
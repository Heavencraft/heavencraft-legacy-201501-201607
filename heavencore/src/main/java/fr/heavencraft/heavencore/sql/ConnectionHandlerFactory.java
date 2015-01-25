package fr.heavencraft.heavencore.sql;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class ConnectionHandlerFactory
{
	public static ConnectionHandler getConnectionHandler(Database database)
	{
		return new DefaultConnectionHandler(database);
	}

	public static ConnectionHandler getConnectionHandler(String database) throws HeavenException
	{
		return getConnectionHandler(Database.getUniqueInstanceByName(database));
	}
}
package fr.heavencraft.heavencore.sql;

import java.util.HashMap;
import java.util.Map;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class ConnectionHandlerFactory
{
	private static final Map<Database, ConnectionProvider> connectionProvidersByDatabase = new HashMap<Database, ConnectionProvider>();

	public static ConnectionProvider getConnectionHandler(Database database)
	{
		ConnectionProvider connectionProvider = connectionProvidersByDatabase.get(database);

		if (connectionProvider == null)
			connectionProvidersByDatabase.put(database, connectionProvider = new HikariConnectionProvider(database));

		return connectionProvider;
	}

	public static ConnectionProvider getConnectionHandler(String database) throws HeavenException
	{
		return getConnectionHandler(Database.getUniqueInstanceByName(database));
	}
}
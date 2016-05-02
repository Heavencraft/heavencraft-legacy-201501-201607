package fr.heavencraft.heavencore.sql;

import java.util.HashMap;
import java.util.Map;

public class ConnectionHandlerFactory
{
	private static final Map<String, ConnectionProvider> connectionProvidersByDatabaseName = new HashMap<String, ConnectionProvider>();

	public static ConnectionProvider getConnectionHandler(Database database)
	{
		ConnectionProvider connectionProvider = connectionProvidersByDatabaseName.get(database.getDatabaseName());

		if (connectionProvider == null)
			connectionProvidersByDatabaseName.put(database.getDatabaseName(),
					connectionProvider = new HikariConnectionProvider(database));

		return connectionProvider;
	}
}
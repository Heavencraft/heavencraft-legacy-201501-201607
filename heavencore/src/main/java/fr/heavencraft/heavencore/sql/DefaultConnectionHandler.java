package fr.heavencraft.heavencore.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.logs.HeavenLog;

public class DefaultConnectionHandler implements ConnectionProvider
{
	private static final long TEST_QUERY_INTERVAL = 600000; // 10 minutes
	private static final String DB_URL = "jdbc:mysql://%1$s:3306/%2$s?user=%3$s&password=%4$s&zeroDateTimeBehavior=convertToNull&autoReconnect=true";
	private static final HeavenLog log = HeavenLog.getLogger(DefaultConnectionHandler.class);

	private final Database database;
	private Connection connection;
	private long lastQuery;

	DefaultConnectionHandler(Database database)
	{
		this.database = database;
	}

	@Override
	public Connection getConnection()
	{
		sendTestQueryIfNeeded();

		try
		{
			if (connection == null || connection.isClosed())
			{
				connection = DriverManager.getConnection(String.format(DB_URL, database.getHost(),
						database.getDatabaseName(), database.getUser(), database.getPassword()));
				log.info("Created connection to database %1$s", database);
			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}

		log.info("Using connection to database %1$s", database);
		lastQuery = System.currentTimeMillis();
		return connection;
	}

	// Send a dumb SQL query, so if the connection is closed, it will be
	// detected by JDBC.
	private void sendTestQueryIfNeeded()
	{
		if (connection != null && System.currentTimeMillis() - lastQuery > TEST_QUERY_INTERVAL)
		{
			try (PreparedStatement ps = connection.prepareStatement("SELECT 1"))
			{
				log.info("Sending test query to %1$s.", database);
				ps.execute();
			}
			catch (final SQLException ex)
			{
				log.info("Connection to database %1$s failed.", database);
			}
		}
	}
}
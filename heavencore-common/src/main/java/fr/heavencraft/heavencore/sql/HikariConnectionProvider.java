package fr.heavencraft.heavencore.sql;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariConnectionProvider implements ConnectionProvider
{
	private final HikariDataSource dataSource;

	HikariConnectionProvider(Database database)
	{
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl(database.getJdbcUrl());
		config.setUsername(database.getUser());
		config.setPassword(database.getPassword());
		config.setMaximumPoolSize(database.getNbConnections());

		dataSource = new HikariDataSource(config);
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
}
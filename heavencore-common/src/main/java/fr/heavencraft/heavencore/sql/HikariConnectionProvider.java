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
		config.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
		config.addDataSourceProperty("serverName", "localhost");
		config.addDataSourceProperty("port", "3306");
		config.addDataSourceProperty("zeroDateTimeBehavior", "convertToNull");
		config.addDataSourceProperty("autoReconnect", "true");

		config.addDataSourceProperty("databaseName", database.getDatabaseName());
		config.setUsername(database.getUsername());
		config.setPassword(database.getPassword());
		config.setMaximumPoolSize(database.getMaximumPoolSize());

		dataSource = new HikariDataSource(config);
	}

	@Override
	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
}
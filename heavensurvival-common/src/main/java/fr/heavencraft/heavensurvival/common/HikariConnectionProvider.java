package fr.heavencraft.heavensurvival.common;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.heavencraft.heavencore.sql.ConnectionProvider;

public class HikariConnectionProvider implements ConnectionProvider
{
	private final HikariDataSource dataSource;

	public HikariConnectionProvider()
	{
		final HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://localhost:3306/minecraft-survie-test?zeroDateTimeBehavior=convertToNull");
		config.setUsername("mc-sql");
		config.setPassword("9e781e41f865901850d5c3060063c8ca");

		dataSource = new HikariDataSource(config);
	}

	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}
}
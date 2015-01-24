package fr.heavencraft.heavencore.sql;

import java.sql.Connection;

import fr.heavencraft.heavencore.providers.Provider;

public interface ConnectionProvider extends Provider
{
	public enum Database
	{
		WEB("mc-db"), //
		PROXY("proxy"), //
		SEMIRP("minecraft-semirp"), //
		TEST("test");

		private final String database;

		private Database(String database)
		{
			this.database = database;
		}

		public String getDatabaseName()
		{
			return database;
		}
	}

	Connection getConnection();
}
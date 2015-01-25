package fr.heavencraft.heavencore.sql;

import java.util.HashMap;
import java.util.Map;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class Database
{
	private static Map<String, Database> databasesByName = new HashMap<String, Database>();

	public static final Database WEB = new Database("mc-db");
	public static final Database PROXY = new Database("proxy");
	public static final Database SEMIRP = new Database("minecraft-semirp");
	public static final Database TEST = new Database("test");

	public static Database getUniqueInstanceByName(String name) throws HeavenException
	{
		final Database database = databasesByName.get(name.toLowerCase());

		if (database == null)
			throw new HeavenException("Invalid database : %1$s", name);

		return database;
	}

	private final String databaseName;

	private Database(String databaseName)
	{
		this.databaseName = databaseName;

		databasesByName.put(databaseName, this);
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	@Override
	public String toString()
	{
		return databaseName;
	}
}
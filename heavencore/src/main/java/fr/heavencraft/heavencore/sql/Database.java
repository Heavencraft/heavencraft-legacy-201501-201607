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
	public static final Database CREATIVE = new Database("minecraft-creative");

	public static final Database UAT_CREATIVE = new Database("minecraft-creative-test");
	public static final Database TEST = new Database("test");

	public static final Database HELLCRAFT_WEB = new Database("91.121.170.189", "hellcraft",
			"CUynkyKVI3CHaCsSPis9", "mc-db");

	public static Database getUniqueInstanceByName(String name) throws HeavenException
	{
		final Database database = databasesByName.get(name.toLowerCase());

		if (database == null)
			throw new HeavenException("Invalid database : %1$s", name);

		return database;
	}

	private final String host;
	private final String user;
	private final String password;
	private final String databaseName;

	private Database(String databaseName)
	{
		this("localhost", "mc-sql", "9e781e41f865901850d5c3060063c8ca", databaseName);
	}

	private Database(String host, String user, String password, String databaseName)
	{
		this.host = host;
		this.user = user;
		this.password = password;
		this.databaseName = databaseName;

		if ("localhost".equals(host))
			databasesByName.put(databaseName, this);
	}

	public String getHost()
	{
		return host;
	}

	public String getUser()
	{
		return user;
	}

	public String getPassword()
	{
		return password;
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
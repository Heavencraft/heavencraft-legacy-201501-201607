package fr.heavencraft.heavencore.sql;

import java.util.HashMap;
import java.util.Map;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class Database
{
	private static Map<String, Database> databasesByName = new HashMap<String, Database>();

	public static final Database WEB = new Database("mc-db", 2);
	public static final Database PROXY = new Database("proxy", 2);
	public static final Database SEMIRP = new Database("minecraft-semirp", 5);
	public static final Database CREATIVE = new Database("minecraft-creative", 5);

	public static final Database UAT_CREATIVE = new Database("minecraft-creative-test", 2);
	public static final Database UAT_SEMIRP = new Database("minecraft-semirp-test", 2);
	public static final Database UAT_SURVIVAL = new Database("minecraft-survie-test", 2);
	public static final Database UAT_FUN = new Database("minecraft-fun-test", 2);

	public static final Database TEST = new Database("test", 1);

	public static final Database HELLCRAFT_WEB = new Database("91.121.170.189", "hellcraft", "CUynkyKVI3CHaCsSPis9",
			"mc-db", 1);

	public static Database getUniqueInstanceByName(String name) throws HeavenException
	{
		final Database database = databasesByName.get(name.toLowerCase());

		if (database == null)
			throw new HeavenException("Invalid database : %1$s", name);

		return database;
	}

	private static final String DB_URL = "jdbc:mysql://%1$s:3306/%2$s?user=%3$s&password=%4$s&zeroDateTimeBehavior=convertToNull&autoReconnect=true";

	private final String host;
	private final String user;
	private final String password;
	private final String databaseName;
	private final int nbConnections;

	private Database(String databaseName, int nbConnections)
	{
		this("localhost", "mc-sql", "9e781e41f865901850d5c3060063c8ca", databaseName, nbConnections);
	}

	private Database(String host, String user, String password, String databaseName, int nbConnections)
	{
		this.host = host;
		this.user = user;
		this.password = password;
		this.databaseName = databaseName;
		this.nbConnections = nbConnections;

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

	public String getJdbcUrl()
	{
		return String.format(DB_URL, host, databaseName, user, password);
	}

	public int getNbConnections()
	{
		return nbConnections;
	}

	@Override
	public String toString()
	{
		return databaseName;
	}
}
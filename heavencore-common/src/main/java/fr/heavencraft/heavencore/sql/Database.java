package fr.heavencraft.heavencore.sql;

public class Database
{
	// TODO: Remove from here
	private static final String MYSQL_USERNAME = "mc-sql";
	private static final String MYSQL_PASSWORD = "0HJ1kmcYZRac985GJIY6Qfg1NoekinUM";
	public static final Database WEB = new Database("mc-db", MYSQL_USERNAME, MYSQL_PASSWORD, 2);
	public static final Database PROXY = new Database("proxy", MYSQL_USERNAME, MYSQL_PASSWORD, 2);
	// to here, and load from config

	private final String databaseName;
	private final String username;
	private final String password;
	private final int maximumPoolSize;

	public Database(String databaseName, String username, String password, int maximumPoolSize)
	{
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
		this.maximumPoolSize = maximumPoolSize;
	}

	public String getDatabaseName()
	{
		return databaseName;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public int getMaximumPoolSize()
	{
		return maximumPoolSize;
	}

	@Override
	public String toString()
	{
		return databaseName;
	}
}
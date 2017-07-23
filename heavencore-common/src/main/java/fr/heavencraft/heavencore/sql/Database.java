package fr.heavencraft.heavencore.sql;

public class Database
{
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
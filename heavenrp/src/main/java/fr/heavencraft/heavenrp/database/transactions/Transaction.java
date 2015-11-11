package fr.heavencraft.heavenrp.database.transactions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Transaction
{
	private final Timestamp timestamp;
	private final int accountId;
	private final int delta;
	private final String log;

	Transaction(ResultSet rs) throws SQLException
	{
		timestamp = rs.getTimestamp("timestamp");
		accountId = rs.getInt("account_id");
		delta = rs.getInt("delta");
		log = rs.getString("log");
	}

	public Timestamp getTimestamp()
	{
		return timestamp;
	}

	public int getDelta()
	{
		return delta;
	}

	public String getLog()
	{
		return log;
	}
}
package fr.heavencraft.heavenfun.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import fr.heavencraft.heavencore.users.balance.UserWithBalance;
import fr.heavencraft.heavenfun.common.HeavenFun;

public class FunUser implements UserWithBalance
{
	private final String uniqueIdAsString;

	private final UUID uniqueId;
	private final String name;
	private final int balance;
	private final Timestamp lastLogin;

	FunUser(ResultSet rs) throws SQLException
	{
		uniqueIdAsString = rs.getString("uuid");

		uniqueId = UUID.fromString(uniqueIdAsString);
		name = rs.getString("name");
		balance = rs.getInt("balance");
		lastLogin = rs.getTimestamp("last_login");
	}

	@Override
	public String getUniqueIdAsString()
	{
		return uniqueIdAsString;
	}

	@Override
	public UUID getUniqueId()
	{
		return uniqueId;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Timestamp getLastLogin()
	{
		return lastLogin;
	}

	@Override
	public int getBalance()
	{
		return balance;
	}

	@Override
	public String getBalanceString()
	{
		return balance + " " + HeavenFun.CURRENCY;
	}

	@Override
	public String getCurrencyName()
	{
		return HeavenFun.CURRENCY;
	}
}
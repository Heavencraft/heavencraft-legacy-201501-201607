package fr.heavencraft.heavenfun.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import fr.heavencraft.heavencore.users.User;
import fr.heavencraft.heavencore.users.balance.UserWithBalance;
import fr.heavencraft.heavencore.users.color.TabColor;

public class FunUser implements User, UserWithBalance
{
	private final String uniqueIdAsString;

	private final UUID uniqueId;
	private final String name;
	private final int balance;

	FunUser(ResultSet rs) throws SQLException
	{
		uniqueIdAsString = rs.getString("uuid");

		uniqueId = UUID.fromString(uniqueIdAsString);
		name = rs.getString("name");
		balance = rs.getInt("balance");
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
	public TabColor getTabColor()
	{
		return TabColor.WHITE;
	}

	@Override
	public int getBalance()
	{
		return balance;
	}
}
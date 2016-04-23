package fr.heavencraft.heavenfun.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.users.UserFactory;

public class FunUserFactory implements UserFactory<FunUser>
{
	@Override
	public FunUser newUser(ResultSet rs) throws SQLException
	{
		return new FunUser(rs);
	}
}
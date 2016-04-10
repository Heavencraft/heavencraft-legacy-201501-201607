package fr.heavencraft.heavensurvival.common.users;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.heavencore.users.UserFactory;

public class SurvivalUserFactory implements UserFactory<SurvivalUser>
{
	@Override
	public SurvivalUser newUser(ResultSet rs) throws SQLException
	{
		return new SurvivalUser(rs);
	}
}
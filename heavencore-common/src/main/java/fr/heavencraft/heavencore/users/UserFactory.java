package fr.heavencraft.heavencore.users;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface UserFactory<U extends User>
{
	public U newUser(ResultSet rs) throws SQLException;
}
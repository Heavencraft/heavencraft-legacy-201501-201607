package fr.heavencraft.heavencore.sql;

import java.sql.Connection;

public interface ConnectionProvider
{
	Connection getConnection();
}
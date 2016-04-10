package fr.heavencraft.async.queries;

import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public interface Query
{
	void executeQuery() throws HeavenException, SQLException;

	void onSuccess();

	void onHeavenException(HeavenException ex);

	void onSQLException(SQLException ex);
}
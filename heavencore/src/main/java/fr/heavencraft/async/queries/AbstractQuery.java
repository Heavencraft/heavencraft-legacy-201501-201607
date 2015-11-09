package fr.heavencraft.async.queries;

import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public abstract class AbstractQuery implements Query
{
	@Override
	public void onSuccess()
	{
	}

	@Override
	public void onHeavenException(HeavenException ex)
	{
	}

	@Override
	public void onSQLException(SQLException ex)
	{
	}
}
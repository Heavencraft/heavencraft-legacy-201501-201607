package fr.heavencraft.heavenfun.common;

import fr.heavencraft.heavencore.sql.ConnectionProvider;

public interface HeavenFun
{
	public static final String CURRENCY = "FP";

	ConnectionProvider getConnectionProvider();
}
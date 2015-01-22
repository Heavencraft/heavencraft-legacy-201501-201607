package fr.heavencraft.heavenguard.datamodel;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.GlobalRegion;

public class SQLGlobalRegion implements GlobalRegion
{
	// Log
	private static final HeavenLog log = HeavenLog.getLogger(SQLGlobalRegion.class);

	private static final String FLAG_PREFIX = "flag_";

	private final String name;
	private final Map<Flag, Boolean> booleanFlags = new HashMap<Flag, Boolean>();

	public SQLGlobalRegion(ResultSet rs) throws SQLException
	{
		name = rs.getString("name");

		// Load flags
		final ResultSetMetaData metadata = rs.getMetaData();
		final int columnCount = metadata.getColumnCount();

		for (int column = 1; column <= columnCount; column++)
		{
			final String columnName = metadata.getColumnName(column);

			if (columnName.startsWith(FLAG_PREFIX))
			{
				final Flag flag = Flag.getUniqueInstanceByName(columnName.substring(FLAG_PREFIX.length()));

				if (flag == null)
				{
					log.warn("Unknown flag %1$s", columnName);
					continue;
				}

				switch (metadata.getColumnType(column))
				{
					case Types.BIT:
						final Boolean booleanValue = rs.getBoolean(columnName);

						if (!rs.wasNull())
							booleanFlags.put(flag, booleanValue);
						break;

					default:
						break;
				}
			}

		}
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public Map<Flag, Boolean> getBooleanFlags()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Boolean getBooleanFlag(Flag flag)
	{
		return booleanFlags.get(flag);
	}

	@Override
	public void setBooleanFlag(Flag flag, Boolean value)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public String toString()
	{
		final StringBuilder str = new StringBuilder();
		str.append(name);
		str.append(" [");

		for (final Iterator<Entry<Flag, Boolean>> iterator = booleanFlags.entrySet().iterator(); iterator.hasNext();)
		{
			final Entry<Flag, Boolean> flag = iterator.next();

			str.append(flag.getKey().getName());
			str.append(": ");
			str.append(flag.getValue());

			if (iterator.hasNext())
				str.append(", ");
		}

		str.append("]");
		return str.toString();
	}
}
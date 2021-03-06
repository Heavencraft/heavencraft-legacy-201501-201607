package fr.heavencraft.heavenguard.datamodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.SQLErrorException;
import fr.heavencraft.heavencore.logs.HeavenLog;
import fr.heavencraft.heavencore.sql.ConnectionProvider;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.FlagHandler;
import fr.heavencraft.heavenguard.api.FlagType;
import fr.heavencraft.heavenguard.api.Region;

public class SQLFlagHandler implements FlagHandler
{
	private static final HeavenLog log = HeavenLog.getLogger(SQLFlagHandler.class);

	private static final String FLAG_PREFIX = "flag_";
	private static final String SET_FLAG = "UPDATE regions SET %1$s = ? WHERE name = LOWER(?) LIMIT 1;";

	private final Region region;
	private final ConnectionProvider connectionHandler;

	public SQLFlagHandler(ConnectionProvider connectionHandler, ResultSet rs, Region region) throws SQLException
	{
		this.region = region;
		this.connectionHandler = connectionHandler;

		final ResultSetMetaData metadata = rs.getMetaData();
		final int columnCount = metadata.getColumnCount();

		for (int column = 1; column <= columnCount; column++)
		{
			final String columnName = metadata.getColumnName(column);

			// Not a flag
			if (!columnName.startsWith(FLAG_PREFIX))
				continue;

			final Flag flag = Flag.getUniqueInstanceByName(columnName.substring(FLAG_PREFIX.length()));

			if (flag == null)
			{
				log.warn("Unknown flag %1$s", columnName);
				continue;
			}

			if (metadata.getColumnType(column) != flag.getType().getSQLType())
			{
				log.warn("Type mismatch : column = %1$s, flag = %2$s", metadata.getColumnType(column),
						flag.getType().getSQLType());
				continue;
			}

			switch (flag.getType())
			{
				case BOOLEAN:
					final Boolean booleanValue = rs.getBoolean(columnName);

					if (!rs.wasNull())
						flags.put(flag, booleanValue);
					break;

				case BYTE_ARRAY:
					final byte[] byteArrayValue = rs.getBytes(columnName);

					if (!rs.wasNull())
						flags.put(flag, byteArrayValue);
					break;

				case TIMESTAMP:
					final Timestamp timestampValue = rs.getTimestamp(columnName);

					if (!rs.wasNull())
						flags.put(flag, timestampValue);
					break;

				default:
					log.warn("Unknown flag type %1$s", flag.getType());
					break;
			}
		}
	}

	private final Map<Flag, Object> flags = new HashMap<Flag, Object>();

	private Object getFlag(Flag flag, boolean heritage)
	{
		final Object value = flags.get(flag);

		if (value != null)
			return value;

		if (heritage)
		{
			final Region parent = region.getParent();

			if (parent != null)
				return ((SQLFlagHandler) parent.getFlagHandler()).getFlag(flag, true);
		}

		return null;
	}

	private void setFlag(Flag flag, Object value) throws HeavenException
	{
		final String query = String.format(SET_FLAG, FLAG_PREFIX + flag.getName());

		try (Connection connection = connectionHandler.getConnection();
				PreparedStatement ps = connection.prepareStatement(query))
		{
			if (value != null)
				ps.setObject(1, value, flag.getType().getSQLType());
			else
				ps.setNull(1, flag.getType().getSQLType());

			ps.setString(2, region.getName());

			if (ps.executeUpdate() != 1)
				throw new HeavenException("Impossible de mettre à jour la région.");

			if (value != null)
				flags.put(flag, value);
			else
				flags.remove(flag);
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
			new SQLErrorException();
		}
	}

	/*
	 * Boolean flags
	 */

	@Override
	public Boolean getBooleanFlag(Flag flag)
	{
		return (Boolean) getFlag(flag, true);
	}

	@Override
	public void setBooleanFlag(Flag flag, Boolean value) throws HeavenException
	{
		if (flag.getType() != FlagType.BOOLEAN)
			throw new HeavenException("Le flag %1$s n'est pas de type Boolean.", flag.getName());

		setFlag(flag, value);
	}

	/*
	 * ByteArray flags
	 */

	@Override
	public byte[] getByteArrayFlag(Flag flag)
	{
		return (byte[]) getFlag(flag, false);
	}

	@Override
	public void setByteArrayFlag(Flag flag, byte[] value) throws HeavenException
	{
		if (flag.getType() != FlagType.BYTE_ARRAY)
			throw new HeavenException("Le flag %1$s n'est pas de type ByteArray.", flag.getName());

		setFlag(flag, value);
	}

	/*
	 * Timestamp flags
	 */

	@Override
	public Timestamp getTimestampFlag(Flag flag)
	{
		return (Timestamp) getFlag(flag, false);
	}

	@Override
	public void setTimestampFlag(Flag flag, Timestamp value) throws HeavenException
	{
		if (flag.getType() != FlagType.TIMESTAMP)
			throw new HeavenException("Le flag %1$s n'est pas de type Timestamp.", flag.getName());

		setFlag(flag, value);
	}

	@Override
	public String toString()
	{
		if (flags.isEmpty())
			return "";

		final StringBuilder str = new StringBuilder("Flags : ");

		for (final Iterator<Entry<Flag, Object>> it = flags.entrySet().iterator(); it.hasNext();)
		{
			final Entry<Flag, Object> flag = it.next();

			str.append(flag.getKey().getName());

			// Don't display ByteArray flags
			if (flag.getKey().getType() != FlagType.BYTE_ARRAY)
				str.append(" : ").append(flag.getValue());

			if (it.hasNext())
				str.append(", ");
		}

		return str.toString();
	}
}
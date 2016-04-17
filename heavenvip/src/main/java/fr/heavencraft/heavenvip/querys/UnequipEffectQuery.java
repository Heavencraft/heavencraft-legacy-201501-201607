package fr.heavencraft.heavenvip.querys;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenvip.HeavenVIP;

public class UnequipEffectQuery extends AbstractQuery
{
	private static final String QUERY = "DELETE FROM vip_equiped WHERE type = ? AND uuid = ? AND descriptor_id = ?";

	private final char type;
	private final String uuid;
	private final int descriptor_id;

	public UnequipEffectQuery(char type, String uuid, int descriptor_id)
	{
		this.type = type;
		this.uuid = uuid;
		this.descriptor_id = descriptor_id;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (PreparedStatement ps = HeavenVIP.getProxyConnection().getConnection().prepareStatement(QUERY))
		{
			ps.setString(1, (String.valueOf(this.type)));
			ps.setString(2, this.uuid);
			ps.setInt(3, this.descriptor_id);
			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
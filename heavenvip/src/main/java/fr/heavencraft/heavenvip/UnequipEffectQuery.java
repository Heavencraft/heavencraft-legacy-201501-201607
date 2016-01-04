package fr.heavencraft.heavenvip;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class UnequipEffectQuery extends AbstractQuery
{
	private static final String QUERY = "DELETE FROM vip_equiped WHERE type = ? AND uuid = ? AND effect_id = ?";

	private final char type;
	private final String uuid;
	private final int effect_id;

	public UnequipEffectQuery(char type, String uuid, int effect_id)
	{
		this.type = type;
		this.uuid = uuid;
		this.effect_id = effect_id;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (PreparedStatement ps = HeavenVIP.getMainConnection().getConnection().prepareStatement(QUERY))
		{
			ps.setString(1, (String.valueOf(this.type)));
			ps.setString(2, this.uuid);
			ps.setInt(3, this.effect_id);
			ps.executeUpdate();
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
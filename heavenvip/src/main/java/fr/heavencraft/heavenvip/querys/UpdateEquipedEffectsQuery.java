package fr.heavencraft.heavenvip.querys;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenvip.HeavenVIP;

public class UpdateEquipedEffectsQuery extends AbstractQuery
{
	private static final String QUERY1 = "SELECT COUNT(*) as count FROM vip_equiped WHERE type = ? AND uuid = ? LIMIT 1";
	private static final String QUERY_INSERT = "INSERT INTO vip_equiped (type, uuid, descriptor_id) VALUES (?, ?, ?)";
	private static final String QUERY_UPDATE = "UPDATE vip_equiped SET descriptor_id = ? WHERE type = ? AND uuid = ?";

	private final char type;
	private final String uuid;
	private final int descriptor_id;
	
	/**
	 * Query to update wich effect you use
	 * @param type
	 * @param uuid
	 * @param descriptor_id
	 */
	public UpdateEquipedEffectsQuery(char type, String uuid, int descriptor_id)
	{
		this.type = type;
		this.uuid = uuid;
		this.descriptor_id = descriptor_id;
	}

	@Override
	public void executeQuery() throws HeavenException, SQLException
	{
		try (PreparedStatement ps1 = HeavenVIP.getMainConnection().getConnection().prepareStatement(QUERY1))
		{
			ps1.setString(1, (String.valueOf(this.type)));
			ps1.setString(2, this.uuid);
			final ResultSet rs = ps1.executeQuery();
			
			while (rs.next())
			{
				// No entry found, insert new
				if(rs.getInt("count") == 0)
				{
					try (PreparedStatement ps2 = HeavenVIP.getMainConnection().getConnection().prepareStatement(QUERY_INSERT))
					{
						ps2.setString(1, (String.valueOf(this.type)));
						ps2.setString(2, this.uuid);
						ps2.setInt(3, this.descriptor_id);
						if (ps2.executeUpdate() == 0)
							throw new HeavenException("Une erreur est survenue.");
					}
					catch (final SQLException ex)
					{
						ex.printStackTrace();
					}
				}
				else
				{
					try (PreparedStatement ps2 = HeavenVIP.getMainConnection().getConnection().prepareStatement(QUERY_UPDATE))
					{
						ps2.setInt(1, this.descriptor_id);
						ps2.setString(2, (String.valueOf(this.type)));
						ps2.setString(3, this.uuid);

						if (ps2.executeUpdate() == 0)
							throw new HeavenException("Une erreur est survenue.");
					}
					catch (final SQLException ex)
					{
						ex.printStackTrace();
					}
				}

			}
		}
		catch (final SQLException ex)
		{
			ex.printStackTrace();
		}
	}
}
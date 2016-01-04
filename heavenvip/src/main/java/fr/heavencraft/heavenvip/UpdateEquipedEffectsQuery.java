package fr.heavencraft.heavenvip;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.async.queries.AbstractQuery;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class UpdateEquipedEffectsQuery extends AbstractQuery
{
	//private static final String QUERY = "IF EXISTS(SELECT 1 FROM vip_equiped WHERE type = ? AND uuid = ? LIMIT 1) " + 
	//		"THEN UPDATE vip_equiped SET effect_id = ? WHERE type = ? AND uuid = ? " +
	//		"ELSE INSERT INTO vip_equiped (type, uuid, effect_id) VALUES (?, ?, ?)";
//TODO Erreur dans la requète SQL
	private static final String QUERY = "CASE WHEN EXISTS(SELECT 1 FROM vip_equiped WHERE type = ? AND uuid = ? LIMIT 1)" +
			"THEN (UPDATE vip_equiped SET effect_id = ? WHERE type = ? AND uuid = ?) " + 
			"ELSE (ELSE INSERT INTO vip_equiped (type, uuid, effect_id) VALUES (?, ?, ?)) " +
			"END";

	private final char type;
	private final String uuid;
	private final int effect_id;

	public UpdateEquipedEffectsQuery(char type, String uuid, int effect_id)
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
			ps.setString(4, (String.valueOf(this.type)));
			ps.setString(5, this.uuid);

			ps.setString(6, (String.valueOf(this.type)));
			ps.setString(7, this.uuid);
			ps.setInt(8, this.effect_id);

			if (ps.executeUpdate() == 0)
				throw new HeavenException("Une erreur est survenue lors de l'équipement.");
		}
	}
}
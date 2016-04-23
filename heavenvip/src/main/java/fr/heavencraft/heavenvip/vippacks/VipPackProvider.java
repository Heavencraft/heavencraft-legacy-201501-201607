package fr.heavencraft.heavenvip.vippacks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenvip.HeavenVIP;

public class VipPackProvider
{
	private final static String SELECT_OWNING_PACKS = "SELECT vip_pack.*, CASE WHEN EXISTS("
			+ "SELECT 1 FROM vip_bought WHERE vip_bought.user_uuid = ? AND vip_bought.pack_id = vip_pack.vip_pack_id LIMIT 1)"
			+ "THEN 1 " + "ELSE 0 " + "END as owns "
			+ "FROM vip_pack WHERE vip_pack.effect_type = ? ORDER BY vip_pack.month ASC";

	private final static String BUY_PACK = "INSERT INTO vip_bought(user_uuid, pack_id, timestamp) VALUES (?, ?, ?)";

	/**
	 * Returns a collection of available packs.
	 * 
	 * @param p
	 * @param type
	 * @return
	 */
	public static Collection<VipPack> getPacks(final Player p, final PackType type)
	{
		Collection<VipPack> packCollection = new ArrayList<VipPack>();

		try (Connection connection = HeavenVIP.getProxyConnection().getConnection();
				PreparedStatement ps = connection.prepareStatement(SELECT_OWNING_PACKS))
		{
			// Inject contextual player values
			ps.setString(1, PlayerUtil.getUUID(p));
			ps.setString(2, String.valueOf(type.getPackType()));
			// Query
			final ResultSet rs = ps.executeQuery();
			while (rs.next())
			{
				VipPack pck = new VipPack(rs.getInt("vip_pack_id"), type,
						ChatColor.translateAlternateColorCodes('§', rs.getString("name")),
						ChatColor.translateAlternateColorCodes('§', rs.getString("description")), rs.getInt("month"),
						rs.getInt("price"), rs.getBoolean("owns"));

				packCollection.add(pck);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return packCollection;
	}

	/**
	 * Add an owned pack to a player
	 * 
	 * @param p
	 * @param packId
	 * @return
	 */
	public static boolean addPack(final Player p, final int packId)
	{
		try (Connection connection = HeavenVIP.getProxyConnection().getConnection();
				PreparedStatement ps = connection.prepareStatement(BUY_PACK))
		{
			// Inject contextual player & pack values
			ps.setString(1, PlayerUtil.getUUID(p));
			ps.setInt(2, packId);
			ps.setLong(3, System.currentTimeMillis());
			// Query
			ps.executeUpdate();
			ps.close();
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}

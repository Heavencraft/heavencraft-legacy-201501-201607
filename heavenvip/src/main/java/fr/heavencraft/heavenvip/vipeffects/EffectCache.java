package fr.heavencraft.heavenvip.vipeffects;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenvip.HeavenVIP;

public class EffectCache
{
	private static final Map<UUID, ArrayList<Integer>> effectsByUUID = new ConcurrentHashMap<UUID, ArrayList<Integer>>();
	private static final String QUERY = "SELECT vip_equiped.type, vip_equiped.effect_id, vip_effects.* FROM vip_equiped, vip_effects " +
		"WHERE vip_equiped.effect_id = vip_effects.vip_effect_id AND vip_equiped.uuid = ? AND vip_equiped.type = 'p'";
	
	public static ArrayList<Integer> getEffectsByUUID(UUID uuid) 
	{
		return effectsByUUID.get(uuid);
	}

	public static void updateCache(Player p) {
		EffectCache.invalidateCache(p);
		// load effects from DB
		
		// "SELECT vip_equiped.* FROM vip_equiped WHERE vip_equiped.uuid = ?"
		try (PreparedStatement ps = HeavenVIP.getMainConnection()
				.getConnection().prepareStatement(QUERY))
		{
			ArrayList<Integer> effect = new ArrayList<Integer>();
			
			ps.setString(1, PlayerUtil.getUUID(p));
			final ResultSet rs = ps.executeQuery();			
			while(rs.next())
			{
				effect.addAll(EffectUtils.translateEffectString(rs.getString("data")));
			}
			effectsByUUID.put(p.getUniqueId(), effect);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public static void invalidateCache(Player p)
	{
		System.out.println("Invalidate EFFECT cache: [" + p.getName() + "]");
		effectsByUUID.remove(p.getUniqueId());
	}

}

package fr.heavencraft.heavenvip.movments;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenvip.HeavenVIP;

public class MovmentEffectCache
{
	private static final Map<UUID, ArrayList<AppliedDescriptorProperties>> effectsByUUID = new HashMap<UUID, ArrayList<AppliedDescriptorProperties>>();
	private static final String QUERY = "SELECT vip_equiped.type, vip_equiped.descriptor_id, vip_movment_descriptors.* FROM vip_equiped, vip_movment_descriptors " +
		"WHERE vip_equiped.descriptor_id = vip_movment_descriptors.vip_movment_descriptor_id AND vip_equiped.uuid = ? AND vip_equiped.type = 'p'";
	private static final String ERROR_LOADING_PARTICLE_EFFECT = "Une erreur est survenue lors de la mise a jour du cache d'effet VIP. Informez un admin.";
	
	public static ArrayList<AppliedDescriptorProperties> getEffectsByUUID(UUID uuid) 
	{
		return effectsByUUID.get(uuid);
	}
	
	public static void updateCache(Player p) {
		MovmentEffectCache.invalidateCache(p);
		// load effects from DB
		
		// "SELECT vip_equiped.* FROM vip_equiped WHERE vip_equiped.uuid = ?"
		try (PreparedStatement ps = HeavenVIP.getProxyConnection()
				.getConnection().prepareStatement(QUERY))
		{
			ArrayList<AppliedDescriptorProperties> effect = new ArrayList<AppliedDescriptorProperties>();
			
			ps.setString(1, PlayerUtil.getUUID(p));
			final ResultSet rs = ps.executeQuery();			
			while(rs.next())
			{
				try
				{
					effect.addAll(EffectDescriptorUtils.translateEffectString(rs.getString("data")));
					effectsByUUID.put(p.getUniqueId(), effect);
				}
				catch (HeavenException e)
				{
					ChatUtil.sendMessage(p, ERROR_LOADING_PARTICLE_EFFECT);
					e.printStackTrace();
				}
			}
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

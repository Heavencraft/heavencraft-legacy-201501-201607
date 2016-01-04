package fr.heavencraft.heavenvip.vipeffects;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenvip.HeavenVIP;

public class EffectProvider
{
	public static VipEffect[] getVipEffectByUser(String uuid) throws HeavenException
	{
		VipEffect effect[] = EffectCache.getEffectsByUUID(uuid);
		if(effect != null)
			return effect;
		
		//TODO load effects from DB
		try (PreparedStatement ps = HeavenVIP.getMainConnection()
				.getConnection().prepareStatement("SELECT vip_equiped.* FROM vip_equiped WHERE vip_equiped.uuid = ?"))
		{
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		
		
		return effect;
	}
	
}

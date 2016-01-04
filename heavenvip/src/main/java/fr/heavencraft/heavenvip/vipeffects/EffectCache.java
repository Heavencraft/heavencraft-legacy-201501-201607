package fr.heavencraft.heavenvip.vipeffects;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;

class EffectCache
{
	private static final Map<String, VipEffect[]> effectsByUUID = new ConcurrentHashMap<String, VipEffect[]>();
	
	public static VipEffect[] getEffectsByUUID(String uuid) 
	{
		return effectsByUUID.get(uuid);
	}
	
	public static void addToCache(Player p, VipEffect[] effect)
	{
		effectsByUUID.put(p.getUniqueId().toString(), effect);
	}

	public static void invalidateCache(Player p)
	{
		System.out.println("Invalidate cache: Effects [" + p.getName() + "]");
		effectsByUUID.remove(p.getUniqueId().toString());
	}
	
}

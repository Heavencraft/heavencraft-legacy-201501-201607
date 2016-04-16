package fr.heavencraft.heavenrp.questframework;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * This class handles storage for playerContext
 * @author Manuel
 *
 */
public class PlayerContextCache
{
	private static Map<UUID, PlayerContext> playerContextStore = new HashMap<UUID, PlayerContext>();
	
	/**
	 * Adds a Player context to the cache
	 * @param p
	 * @param context
	 */
	public static void addPlayerContext(UUID p, PlayerContext context)
	{
		PlayerContextCache.playerContextStore.put(p, context);
	}
	
	/**
	 * Returns a PlayerContext from the cache
	 * @param p
	 * @return
	 */
	public static PlayerContext getPlayerContext(UUID p)
	{
		return PlayerContextCache.playerContextStore.get(p);
		
	}
	
	/**
	 * Deletes a PlayerContext from the cache
	 * @param p
	 * @return
	 */
	public static void removePlayerContext(UUID p)
	{
		PlayerContextCache.playerContextStore.remove(p);
	}

}

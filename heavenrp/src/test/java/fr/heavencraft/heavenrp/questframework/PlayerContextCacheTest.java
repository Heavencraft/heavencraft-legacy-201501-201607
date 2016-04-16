package fr.heavencraft.heavenrp.questframework;

import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import junit.framework.TestCase;

public class PlayerContextCacheTest extends TestCase
{
	public void testPlayerContextCache() throws HeavenException
	{
		UUID uid1 = UUID.randomUUID();
		UUID uid2 = UUID.randomUUID();
		UUID uid3 = UUID.randomUUID();
		PlayerContext ctx1 = new PlayerContext();
		PlayerContext ctx2 = new PlayerContext();
		PlayerContext ctx3 = new PlayerContext();

		assertTrue("Context should be empty", PlayerContextCache.getPlayerContext(uid1) == null);
		PlayerContextCache.addPlayerContext(uid1, ctx1);
		PlayerContextCache.addPlayerContext(uid2, ctx2);
		PlayerContextCache.addPlayerContext(uid3, ctx3);
		assertSame("Should return the same PlayerContext", ctx1, PlayerContextCache.getPlayerContext(uid1));
		assertSame("Should return the same PlayerContext", ctx2, PlayerContextCache.getPlayerContext(uid2));
		assertSame("Should return the same PlayerContext", ctx3, PlayerContextCache.getPlayerContext(uid3));
		PlayerContextCache.removePlayerContext(uid2);
		assertSame("Should return the same PlayerContext", ctx1, PlayerContextCache.getPlayerContext(uid1));
		assertTrue("This context should no longer be in cache", PlayerContextCache.getPlayerContext(uid2) == null);
		assertSame("Should return the same PlayerContext", ctx3, PlayerContextCache.getPlayerContext(uid3));

	}
}

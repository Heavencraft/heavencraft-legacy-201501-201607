package fr.heavencraft.heavenrp.questframework;

import fr.heavencraft.heavenrp.exceptions.QuestFlagCollisionException;
import fr.heavencraft.heavenrp.exceptions.QuestFlagTypeException;
import fr.heavencraft.heavenrp.exceptions.UnknownQuestFlagException;
import junit.framework.TestCase;

public class PlayerContextTest extends TestCase
{
	public void testPlayerContextFlagStore()
	{
		PlayerContext ctx = new PlayerContext();

		// Add flags
		QfFlag flag1 = new QfFlag("flag1");
		try
		{
			ctx.addFlag(flag1, true);
			assertTrue("We should have the flag now", ctx.hasFlag(flag1));
		}
		catch (QuestFlagCollisionException e)
		{
			fail("Player context flag collision detected, but there should not be one.");
		}
		QfFlag flag2 = new QfFlag("flag2");
		try
		{
			ctx.addFlag(flag2, "wow");
			assertTrue("We should have the flag now", ctx.hasFlag(flag2));
		}
		catch (QuestFlagCollisionException e)
		{
			fail("Player context flag collision detected, but there should not be one.");
		}

		QfFlag flag3 = new QfFlag("flag3");
		try
		{
			ctx.addFlag(flag3, 3);
			assertTrue("We should have the flag now", ctx.hasFlag(flag3));
		}
		catch (QuestFlagCollisionException e)
		{
			fail("Player context flag collision detected, but there should not be one.");
		}

		// Flag Collision
		QfFlag flag3Collision = new QfFlag("flag3");
		try
		{
			ctx.addFlag(flag3Collision, 2);
			fail("Player context flag collision NOT detected");
		}
		catch (QuestFlagCollisionException e)
		{
			assertFalse("Player context flag collision detected, but there should not be one.", e == null);
		}

		// Has Flag
		QfFlag flag4 = new QfFlag("notAddedFlag");
		assertFalse("We should not have the flag now", ctx.hasFlag(flag4));

		// Check if we can get back the value
		Object o1 = ctx.getValue(flag1);
		assertTrue("Flag 1 should be a boolean", o1 instanceof Boolean);
		assertFalse("Flag 1 should not be an instance of integer", o1 instanceof Integer);
		assertFalse("Flag 1 should not be an instance of String", o1 instanceof String);
		Boolean v1 = (Boolean) o1;
		assertTrue("Flag 1 was true", v1);

		Object o2 = ctx.getValue(flag2);
		assertFalse("Flag 2 should not be an instance boolean", o2 instanceof Boolean);
		assertFalse("Flag 2 should not be an instance of integer", o2 instanceof Integer);
		assertTrue("Flag 2 should be an instance of String", o2 instanceof String);
		String v2 = (String) o2;
		assertEquals("Flag 2 has the wrong value", "wow", v2);

		Object o3 = ctx.getValue(flag3);
		assertFalse("Flag 3 should not be an instance boolean", o3 instanceof Boolean);
		assertTrue("Flag 3 should be an instance of integer", o3 instanceof Integer);
		assertFalse("Flag 3 should not be an instance of String", o3 instanceof String);
		int v3 = (Integer) o3;
		assertFalse("Flag 3 has changed value because of flag collision", 2 == v3);
		assertEquals("Flag 3 has the wrong value", 3, v3);
		try
		{
			ctx.updateFlag(flag1, false);
			assertEquals("Flag value not updated", false, (boolean) ctx.getValue(flag1));
		}
		catch (UnknownQuestFlagException | QuestFlagTypeException e)
		{
			fail("Could not update Boolean typed flag:" + e.getMessage());
		}

		try
		{
			ctx.updateFlag(flag2, "cookies");
			assertEquals("Flag value not updated", "cookies", (String) ctx.getValue(flag2));
		}
		catch (UnknownQuestFlagException | QuestFlagTypeException e)
		{
			fail("Could not update String typed flag:" + e.getMessage());
		}

		try
		{
			ctx.updateFlag(flag3, 4);
			assertEquals("Flag value not updated", 4, (int) ctx.getValue(flag3));
		}
		catch (UnknownQuestFlagException | QuestFlagTypeException e)
		{
			fail("Could not update Integer typed flag:" + e.getMessage());
		}

		try
		{
			ctx.updateFlag(flag3, "no!");
			fail("Flag was able to change type");
		}
		catch (UnknownQuestFlagException | QuestFlagTypeException e)
		{
			assertTrue("We should have got a Quest Flag Type Exception:", e instanceof QuestFlagTypeException);
			assertFalse("Wrong Exception thrown on flag type change:", e instanceof UnknownQuestFlagException);
			assertTrue("Quest Flag Exception has no message", e.getMessage() != null);
		}
	}
}

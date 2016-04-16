package fr.heavencraft.heavenrp.questframework;

import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import junit.framework.TestCase;

public class QuestContextListCacheTest extends TestCase
{
	public void testQuestContextListCache() throws HeavenException
	{
		UUID uid1 = UUID.randomUUID();
		UUID uid2 = UUID.randomUUID();
		UUID uid3 = UUID.randomUUID();

		QuestContext c1c1 = new QuestContext(1);
		QuestContext c1c2 = new QuestContext(2);
		QuestContext c1c3 = new QuestContext(3);

		QuestContext c2c1 = new QuestContext(4);
		QuestContext c2c2 = new QuestContext(5);
		QuestContext c2c3 = new QuestContext(6);

		QuestContext c3c1 = new QuestContext(7);
		QuestContext c3c2 = new QuestContext(8);
		QuestContext c3c3 = new QuestContext(9);

		assertNotNull("Context list should be empty, not null",
				QuestContextListCache.getInstance().getQuestContextList(uid1));
		assertEquals("Context list should be empty", 0,
				QuestContextListCache.getInstance().getQuestContextList(uid1).size());
		QuestContextListCache.getInstance().addQuestContext(uid1, c1c1);
		QuestContextListCache.getInstance().addQuestContext(uid1, c1c2);
		QuestContextListCache.getInstance().addQuestContext(uid1, c1c3);

		QuestContextListCache.getInstance().addQuestContext(uid2, c2c1);
		QuestContextListCache.getInstance().addQuestContext(uid2, c2c2);
		QuestContextListCache.getInstance().addQuestContext(uid2, c2c3);

		QuestContextListCache.getInstance().addQuestContext(uid3, c3c1);
		QuestContextListCache.getInstance().addQuestContext(uid3, c3c2);
		QuestContextListCache.getInstance().addQuestContext(uid3, c3c3);

		// Test if we can add new items
		assertSame("Should return the same QuestContext", c1c1,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(0));
		assertSame("Should return the same QuestContext", c1c2,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(1));
		assertSame("Should return the same QuestContext", c1c3,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(2));

		assertSame("Should return the same QuestContext", c2c1,
				QuestContextListCache.getInstance().getQuestContextList(uid2).get(0));
		assertSame("Should return the same QuestContext", c2c2,
				QuestContextListCache.getInstance().getQuestContextList(uid2).get(1));
		assertSame("Should return the same QuestContext", c2c3,
				QuestContextListCache.getInstance().getQuestContextList(uid2).get(2));

		assertSame("Should return the same QuestContext", c3c1,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(0));
		assertSame("Should return the same QuestContext", c3c2,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(1));
		assertSame("Should return the same QuestContext", c3c3,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(2));

		// Test if we can remove a list of quest contexts
		QuestContextListCache.getInstance().removeQuestContextList(uid2);
		assertSame("Should return the same QuestContext", c1c1,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(0));
		assertSame("Should return the same QuestContext", c1c2,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(1));
		assertSame("Should return the same QuestContext", c1c3,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(2));

		assertEquals("This context should no longer be in cache", 0,
				QuestContextListCache.getInstance().getQuestContextList(uid2).size());

		assertSame("Should return the same QuestContext", c3c1,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(0));
		assertSame("Should return the same QuestContext", c3c2,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(1));
		assertSame("Should return the same QuestContext", c3c3,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(2));

		// Test if we can remove a certain context from list
		QuestContextListCache.getInstance().removeQuestContext(uid1, c1c2);

		assertEquals("The size of this list should have been reduced", 2,
				QuestContextListCache.getInstance().getQuestContextList(uid1).size());
		assertSame("Should return the same QuestContext", c1c1,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(0));
		assertSame("Should return the same QuestContext", c1c3,
				QuestContextListCache.getInstance().getQuestContextList(uid1).get(1));

		assertSame("Should return the same QuestContext", c3c1,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(0));
		assertSame("Should return the same QuestContext", c3c2,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(1));
		assertSame("Should return the same QuestContext", c3c3,
				QuestContextListCache.getInstance().getQuestContextList(uid3).get(2));

	}
}

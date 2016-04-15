package fr.heavencraft.heavenrp.questframework;

import java.util.Collection;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.quests.SampleQuest;
import junit.framework.TestCase;

public class QuestCacheTest extends TestCase
{
	public void testQuestCache() throws HeavenException
	{
		SampleQuest sc = new SampleQuest();
		UUID pUid = UUID.randomUUID();

		Collection<AbstractQuest> list = QuestCache.GetQuests(pUid);
		assertEquals("Player should have no quests", 0, list.size());
		// Add a quest
		QuestCache.RegisterQuest(pUid, sc);
		list = QuestCache.GetQuests(pUid);
		assertEquals("Player should have no quests", 1, list.size());
		// TODO CONTINUE TESTING HERE
		// assertFalse("Quest Cache should not contain this flag now", );

	}
}

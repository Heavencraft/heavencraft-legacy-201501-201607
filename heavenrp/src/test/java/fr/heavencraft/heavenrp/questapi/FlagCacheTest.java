package fr.heavencraft.heavenrp.questapi;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import junit.framework.TestCase;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.questframework.AbstractQuest;
import fr.heavencraft.heavenrp.questframework.QuestCache;
import fr.heavencraft.heavenrp.questframework.QuestFlag;
import fr.heavencraft.heavenrp.quests.SampleQuest;

public class FlagCacheTest extends TestCase
{
	SampleQuest sc = new SampleQuest();
	
	UUID id = UUID.randomUUID();
	QuestFlag flag1 = new QuestFlag(id);
	QuestFlag flag2 = new QuestFlag(UUID.randomUUID());
	QuestFlag flag3 = new QuestFlag(UUID.randomUUID());

	public void testGetUUID() throws HeavenException
	{
		assertEquals("Not the same (value) UUID", id, flag1.getFlagId());
		assertSame("Not the same (reference) UUID", id, flag1.getFlagId());
		assertNotSame("Two different flag have same id", flag2.getFlagId(), flag1.getFlagId());
	}
	
	public void testCache() throws HeavenException
	{
		UUID pUid = UUID.randomUUID();
		
		Collection<AbstractQuest> list = QuestCache.GetQuests(pUid);
		assertEquals("Player should have no quests", 0, list.size());
		// Add a quest
		QuestCache.RegisterQuest(pUid, sc);
		list = QuestCache.GetQuests(pUid);
		assertEquals("Player should have no quests", 1, list.size());
		//TODO CONTINUE TESTING HERE
		//assertFalse("Quest Cache should not contain this flag now", );
		
	}
}

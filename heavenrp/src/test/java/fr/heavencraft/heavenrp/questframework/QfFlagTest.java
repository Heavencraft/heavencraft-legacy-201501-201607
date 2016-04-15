package fr.heavencraft.heavenrp.questframework;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import junit.framework.TestCase;

public class QfFlagTest extends TestCase
{

	public void testQuestFlag() throws HeavenException
	{
		String flg = "flag1";
		QfFlag flag1 = new QfFlag(flg);
		QfFlag flag2 = new QfFlag("flag2");
		assertEquals("Not the same (value) UUID", "flag1", flag1.getKey());
		assertSame("Not the same (reference) UUID", flg, flag1.getKey());
		assertNotSame("Two different flag have same ", flag2.getKey(), flag1.getKey());
	}

}

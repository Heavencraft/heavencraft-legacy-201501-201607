package fr.heavencraft.heavenrp.jobs;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import junit.framework.TestCase;

public class JobActionTypeTest extends TestCase
{
	public void testGetActionTypeByCode() throws HeavenException
	{
		assertEquals(JobActionType.BAKE, JobActionType.getActionTypeByCode('S'));
		assertEquals(JobActionType.BREAK, JobActionType.getActionTypeByCode('B'));
		assertEquals(JobActionType.CRAFT, JobActionType.getActionTypeByCode('C'));
		assertEquals(JobActionType.KILL, JobActionType.getActionTypeByCode('K'));
		assertEquals(JobActionType.FISH, JobActionType.getActionTypeByCode('P'));
	}

}
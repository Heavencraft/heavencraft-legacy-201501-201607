package fr.heavencraft.heavenrp.jobs;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import junit.framework.TestCase;

public class JobActionTypeTest extends TestCase
{
	public void testGetByCode()
	{
		assertEquals(JobActionType.BAKE, JobActionType.getActionTypeByCode('S'));
		assertEquals(JobActionType.BREAK, JobActionType.getActionTypeByCode('B'));
		assertEquals(JobActionType.CRAFT, JobActionType.getActionTypeByCode('C'));
		assertEquals(JobActionType.KILL, JobActionType.getActionTypeByCode('K'));
		assertEquals(JobActionType.FISH, JobActionType.getActionTypeByCode('P'));
	}

	public void testCreateParamFromString()
	{
		assertEquals(Material.STONE, JobActionType.BREAK.createParamFromString("STONE"));
		assertEquals(EntityType.CREEPER, JobActionType.KILL.createParamFromString("CREEPER"));
	}
}
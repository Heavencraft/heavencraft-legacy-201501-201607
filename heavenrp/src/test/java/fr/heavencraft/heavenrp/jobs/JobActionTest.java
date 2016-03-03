package fr.heavencraft.heavenrp.jobs;

import org.bukkit.Material;

import junit.framework.TestCase;

public class JobActionTest extends TestCase
{
	public void testEquals()
	{
		final JobAction action = new JobAction(JobActionType.BREAK, Material.STONE);

		assertTrue(action.equals(new JobAction(JobActionType.BREAK, Material.STONE)));
		assertFalse(action.equals(new JobAction(JobActionType.BAKE, Material.STONE)));
		assertFalse(action.equals(new JobAction(JobActionType.BREAK, Material.COBBLESTONE)));
	}
}
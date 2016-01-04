package fr.heavencraft.heavenrp.jobs;

import junit.framework.TestCase;

public class JobUtilTest extends TestCase
{
	public void testGetLevelFromXp()
	{
		// At level 498, the optimized method is shifted of 1xp (limitation of double mantissa)
		for (int level = 1; level != 498; level++)
		{
			long minXp = getXpToLevel(level);
			long maxXp = getXpToLevel(level + 1) - 1;

			System.out.println("Level " + level + ": [" + minXp + " - " + maxXp + "]");
			assertEquals(level, JobUtil.getLevelFromXp(minXp));
			assertEquals(level, JobUtil.getLevelFromXp(maxXp));
		}
	}

	// From Balthov's definition, not optimized, used to test the optimized method only
	private static long getXpToLevel(int level)
	{
		double sum = 0;
		for (int n = 0; n != level - 1; n++)
		{
			sum += 100d * Math.pow(1.05, n);
		}
		return (long) Math.ceil(sum);
	}
}
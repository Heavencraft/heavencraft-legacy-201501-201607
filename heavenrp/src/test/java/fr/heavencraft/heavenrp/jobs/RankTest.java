package fr.heavencraft.heavenrp.jobs;

import junit.framework.TestCase;

public class RankTest extends TestCase
{
	public void testDisplayName()
	{
		assertEquals("Apprenti", Rank.APPRENTICE.getDisplayName());
		assertEquals("Novice", Rank.NOVICE.getDisplayName());
		assertEquals("Intermediaire", Rank.INTERMEDIATE.getDisplayName());
		assertEquals("Professionnel", Rank.PROFESSIONAL.getDisplayName());
		assertEquals("Maitre", Rank.MASTER.getDisplayName());
	}

	public void testPointsMultiplier()
	{
		assertEquals(1.0, Rank.APPRENTICE.getPointsMultiplier());
		assertEquals(1.1, Rank.NOVICE.getPointsMultiplier());
		assertEquals(1.2, Rank.INTERMEDIATE.getPointsMultiplier());
		assertEquals(1.3, Rank.PROFESSIONAL.getPointsMultiplier());
		assertEquals(1.4, Rank.MASTER.getPointsMultiplier());
	}

	public void testGetRankByLevel()
	{
		assertEquals(Rank.APPRENTICE, Rank.getRankByLevel(0));
		assertEquals(Rank.APPRENTICE, Rank.getRankByLevel(4));
		assertEquals(Rank.NOVICE, Rank.getRankByLevel(5));
		assertEquals(Rank.NOVICE, Rank.getRankByLevel(24));
		assertEquals(Rank.INTERMEDIATE, Rank.getRankByLevel(25));
		assertEquals(Rank.INTERMEDIATE, Rank.getRankByLevel(49));
		assertEquals(Rank.PROFESSIONAL, Rank.getRankByLevel(50));
		assertEquals(Rank.PROFESSIONAL, Rank.getRankByLevel(99));
		assertEquals(Rank.MASTER, Rank.getRankByLevel(100));
	}
}
package fr.heavencraft.heavenrp.jobs;

public class JobUtil
{
	private static final double U0 = 100; // Xp to level 2
	private static final double XP_MULTIPLIER = 1.05;

	// Xp needed to go to the next level :
	// U0 = 100
	// U1 = U0 * 1,05
	// U2 = U1 * 1,05 = U0 * 1,05^2
	// ----------------------------------
	// Un = U0 * 1,05^n

	// Total Xp needed to go to a level from 0 :
	// L0 = U0
	// L1 = U0 + U1 = U0 + (U0 * 1,05) = U0 * (1 + 1,05)
	// L2 = U0 + U1 + U2 = U0 + (U0 * 1,05) + (U0 * 1,05^2) = U0 * (1 + 1,05 + 1,05^2)
	// ----------------------------------
	// L(n) = U0 * P(n)
	// P(n) = 1 + 1,05 * P(n-1)

	public static int getLevelFromXp(long xp)
	{
		int level = 1;
		double levelXp = 1;
		double xpLimit = xp / U0;

		while (true)
		{
			if (levelXp > xpLimit)
				return level;

			level++;
			levelXp = 1 + XP_MULTIPLIER * levelXp;
		}
	}
}
package fr.heavencraft.heavenrp.jobs;

public enum Rank
{
	APPRENTICE("Apprenti", 1),
	NOVICE("Novice", 1.1),
	INTERMEDIATE("Intermediaire", 1.2),
	PROFESSIONAL("Professionnel", 1.3),
	MASTER("Maitre", 1.4);

	private final String displayName;
	private final double pointsMultiplier;

	private Rank(String displayName, double pointsMultiplier)
	{
		this.displayName = displayName;
		this.pointsMultiplier = pointsMultiplier;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public double getPointsMultiplier()
	{
		return pointsMultiplier;
	}

	public static Rank getRankByLevel(int level)
	{
		if (level >= 100)
			return MASTER;
		else if (level >= 50)
			return PROFESSIONAL;
		else if (level >= 25)
			return INTERMEDIATE;
		else if (level >= 5)
			return NOVICE;
		else
			return APPRENTICE;
	}
}
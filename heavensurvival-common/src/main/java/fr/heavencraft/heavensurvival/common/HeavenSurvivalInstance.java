package fr.heavencraft.heavensurvival.common;

public class HeavenSurvivalInstance
{
	private static HeavenSurvival instance;

	public static HeavenSurvival get()
	{
		return instance;
	}

	public static void set(HeavenSurvival instance)
	{
		HeavenSurvivalInstance.instance = instance;
	}
}
package fr.heavencraft.heavenfun.common;

public class HeavenFunInstance
{
	private static HeavenFun instance;

	public static HeavenFun get()
	{
		return instance;
	}

	public static void set(HeavenFun instance)
	{
		HeavenFunInstance.instance = instance;
	}
}
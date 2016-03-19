package fr.heavencraft.heavencore.logs;

public class BukkitHeavenLogFactory implements HeavenLogFactory
{
	@Override
	public HeavenLog newHeavenLog(Class<?> clazz)
	{
		return new BukkitHeavenLog(clazz);
	}
}
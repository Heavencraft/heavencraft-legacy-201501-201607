package fr.heavencraft.heavencore.logs;

public interface HeavenLogFactory
{
	HeavenLog newHeavenLog(Class<?> clazz);
}
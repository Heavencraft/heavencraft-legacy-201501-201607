package fr.heavencraft.heavencore.logs;

public abstract class HeavenLog
{
	private static HeavenLogFactory factory;

	public static void setFactory(HeavenLogFactory factory)
	{
		HeavenLog.factory = factory;
	}

	public static HeavenLog getLogger(Class<?> clazz)
	{
		return factory.newHeavenLog(clazz);
	}

	public abstract void enableDebug();

	public abstract void debug(String format, Object... args);

	public abstract void info(String format, Object... args);

	public abstract void warn(String format, Object... args);

	public abstract void error(String format, Object... args);
}
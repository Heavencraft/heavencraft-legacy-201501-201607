package fr.heavencraft.heavencore;

import org.bukkit.Bukkit;

public class BukkitTickCounterTask implements Runnable
{
	private static long counter = 0;

	BukkitTickCounterTask(CorePlugin plugin)
	{
		Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 1);
	}

	@Override
	public void run()
	{
		counter++;
	}

	public static long getCounter()
	{
		return counter;
	}
}
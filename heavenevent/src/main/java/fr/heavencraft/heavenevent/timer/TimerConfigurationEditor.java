package fr.heavencraft.heavenevent.timer;

import org.bukkit.configuration.MemorySection;

import fr.heavencraft.heavenevent.HeavenEvent;

public class TimerConfigurationEditor
{
	static boolean start;
	static Object time;

	/**
	 * Test if Config is empty and in this case load the default config
	 */
	static void tryConfig()
	{
		if (!getConfig().contains("start"))
			getConfig().set("start", false);
		if (!getConfig().contains("time"))
			getConfig().set("time", 0);
		saveConfig();
	}

	/**
	 * convert the current time in day , hours, minutes and seconds, save it and return the date to the player
	 * 
	 * @return
	 */
	static void saveCurrentTime()
	{
		getConfig().set("start", true);

		getConfig().set("time", System.currentTimeMillis());
		saveConfig();
	}

	static void saveFinalTime()
	{
		getConfig().set("D", TimerScoreboard.currentDay);
		getConfig().set("H", TimerScoreboard.currentHours);
		getConfig().set("M", TimerScoreboard.currentMinutes);
		getConfig().set("S", TimerScoreboard.currentSeconds);
		saveConfig();
	}

	static void resetConfig()
	{
		getConfig().set("start", false);
		getConfig().set("time", 0);
		saveConfig();

	}

	private static void saveConfig()
	{
		HeavenEvent.getInstance().saveConfig();

		start = (boolean) getConfig().get("start");
		time = getConfig().get("time");
	}

	private static MemorySection getConfig()
	{
		return HeavenEvent.getInstance().getConfig();
	}

}

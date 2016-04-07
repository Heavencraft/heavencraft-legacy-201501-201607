package fr.heavencraft.heavenevent.timer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.configuration.MemorySection;

import fr.heavencraft.heavenevent.HeavenEvent;

public class TimerConfigurationEditor
{
	static int day;
	static int hours;
	static int minutes;
	static int seconds;
	static boolean start;

	/**
	 * Test if Config is empty and in this case load the default config
	 */
	static void loadDefaultConfig()
	{
		if (!getConfig().contains("start"))
			getConfig().set("start", false);
		if (!getConfig().contains("day"))
			getConfig().set("day", 0);
		if (!getConfig().contains("hours"))
			getConfig().set("hours", 0);
		if (!getConfig().contains("minutes"))
			getConfig().set("minutes", 0);
		if (!getConfig().contains("seconds"))
			getConfig().set("seconds", 0);
		saveConfig();
	}

	/**
	 * convert the current time in day , hours, minutes and seconds, save it and return the date to the player
	 * 
	 * @return
	 */
	static String saveCurrentTime()
	{
		final Calendar myCalendar = GregorianCalendar.getInstance();

		getConfig().set("start", true);

		getConfig().set("day", myCalendar.get(Calendar.DAY_OF_YEAR));
		getConfig().set("hours", myCalendar.get(Calendar.HOUR_OF_DAY));
		getConfig().set("minutes", myCalendar.get(Calendar.MINUTE));
		getConfig().set("seconds", myCalendar.get(Calendar.SECOND));
		saveConfig();

		return ("données sauvegardées : " + "jours : " + day + ", heures : " + hours + ", minutes : " + minutes
				+ ", seconds : " + seconds);
	}

	static void resetConfig()
	{
		getConfig().set("start", false);
		getConfig().set("day", 0);
		getConfig().set("hours", 0);
		getConfig().set("minutes", 0);
		getConfig().set("seconds", 0);
		saveConfig();

	}

	private static void saveConfig()
	{
		HeavenEvent.getInstance().saveConfig();

		day = (int) getConfig().get("day");
		hours = (int) getConfig().get("hours");
		minutes = (int) getConfig().get("minutes");
		seconds = (int) getConfig().get("seconds");
		start = (boolean) getConfig().get("start");
	}

	private static MemorySection getConfig()
	{
		return HeavenEvent.getInstance().getConfig();
	}

}

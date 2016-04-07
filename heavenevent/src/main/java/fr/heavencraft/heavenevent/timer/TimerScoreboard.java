package fr.heavencraft.heavenevent.timer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.heavencraft.heavenevent.HeavenEvent;

public class TimerScoreboard
{
	private final static String TIMERTITLE = ChatColor.GRAY + "  -*-*-*-*-*-*-";
	private final static String OBJECTIVETITLE = ChatColor.AQUA + "Infos " + ChatColor.GOLD + "Event";

	private static ScoreboardManager manager = Bukkit.getScoreboardManager();
	private static Scoreboard timer = manager.getNewScoreboard();
	private static Objective objective;

	private static BukkitScheduler updateTimer = Bukkit.getServer().getScheduler();

	private static int currentDay;
	private static int currentHours;
	private static int currentMinutes;
	private static int currentSeconds;

	/**
	 * initialize the Scoreboard timer
	 */
	static void initScoreboard()
	{
		// define objective timer
		objective = timer.registerNewObjective("Event", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(OBJECTIVETITLE);

		// get the time since event start
		getCurrentTime();

		// set points
		objective.getScore(TIMERTITLE).setScore(0);
		objective.getScore("Jours : " + currentDay).setScore(-1);
		objective.getScore("Heures : " + currentHours).setScore(-2);
		objective.getScore("Minutes : " + currentMinutes).setScore(-3);
		objective.getScore("Secondes : " + currentSeconds).setScore(-4);

		// launch Scoreboard repetable task
		runScoreboard();

	}

	/**
	 * Get the time since the event start
	 */
	static void getCurrentTime()
	{
		final Calendar myCalendar = GregorianCalendar.getInstance();

		currentDay = myCalendar.get(Calendar.DAY_OF_YEAR) - TimerConfigurationEditor.day;

		currentHours = myCalendar.get(Calendar.HOUR_OF_DAY) - TimerConfigurationEditor.hours;
		if (currentHours < 0)
		{
			currentDay -= 1;
			currentHours += 24;
		}

		currentMinutes = myCalendar.get(Calendar.MINUTE) - TimerConfigurationEditor.minutes;
		if (currentMinutes < 0)
		{
			currentHours -= 1;
			currentMinutes += 60;
		}

		currentSeconds = myCalendar.get(Calendar.SECOND) - TimerConfigurationEditor.seconds;
		if (currentSeconds < 0)
		{
			currentMinutes -= 1;
			currentSeconds += 60;
		}
	}

	/**
	 * run Scoreboard timer
	 */
	static void runScoreboard()
	{
		// set scoreboard for all connected player
		for (final Player online : Bukkit.getOnlinePlayers())
			online.setScoreboard(timer);

		// launch Timer
		updateTimer.scheduleSyncRepeatingTask(HeavenEvent.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				currentSeconds += 1;
				if (currentSeconds >= 60)
				{
					resetTimer("Secondes : ", currentSeconds, -4);
					currentMinutes += 1;
					updateTimer("Minutes : ", currentMinutes, -3);
					currentSeconds = 0;
				}
				else
					updateTimer("Secondes : ", currentSeconds, -4);

				if (currentMinutes >= 60)
				{
					resetTimer("Minutes : ", currentMinutes, -3);
					currentHours += 1;
					updateTimer("Heures : ", currentHours, -2);
					currentMinutes = 0;
				}
				else
					updateTimer("Minutes : ", currentMinutes, -3);

				if (currentHours >= 24)
				{
					resetTimer("Heures : ", currentHours, -2);
					currentDay += 1;
					updateTimer("Jours : ", currentDay, -1);
					currentHours = 0;
				}
				else
					updateTimer("Heures : ", currentHours, -2);
			}

		}, 20L, 20L);

	}

	/**
	 * reset Timer line
	 * 
	 * @param name
	 *            line to update
	 * @param time
	 *            time to update
	 */
	static void resetTimer(String name, Integer time, Integer index)
	{
		timer.resetScores(name + (time - 1));
		objective.getScore(name + 0).setScore(index);

	}

	/**
	 * update scoreboard
	 * 
	 * @param name
	 *            line to update
	 * @param time
	 *            time to update
	 */
	static void updateTimer(final String name, Integer time, Integer index)
	{
		timer.resetScores(name + (time - 1));
		objective.getScore(name + time).setScore(index);
	}

	static void stopScoreboard()
	{
		updateTimer.cancelAllTasks();
		objective.unregister();
	}

	public static Scoreboard getTimer()
	{
		return timer;
	}

}

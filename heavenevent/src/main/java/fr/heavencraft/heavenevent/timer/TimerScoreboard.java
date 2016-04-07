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
	static ScoreboardManager manager = Bukkit.getScoreboardManager();
	static Scoreboard timer = manager.getNewScoreboard();
	static Objective objective;

	static BukkitScheduler updateTimer = Bukkit.getServer().getScheduler();

	static int currentDay;
	static int currentHours;
	static int currentMinutes;
	static int currentSeconds;

	/**
	 * initialize the Scoreboard timer
	 */
	static void initScoreboard()
	{
		objective = timer.registerNewObjective("Event", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.AQUA + "Infos " + ChatColor.GOLD + "Event");

		getCurrentTime();

		objective.getScore(ChatColor.GRAY + "  -*-*-*-*-*-*-").setScore(0);
		objective.getScore("Jours : " + currentDay).setScore(-1);
		objective.getScore("Heures : " + currentHours).setScore(-2);
		objective.getScore("Minutes : " + currentMinutes).setScore(-3);
		objective.getScore("Secondes : " + currentSeconds).setScore(-4);

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
		for (final Player online : Bukkit.getOnlinePlayers())
			online.setScoreboard(timer);
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

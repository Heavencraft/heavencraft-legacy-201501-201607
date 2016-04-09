package fr.heavencraft.heavenevent.timer;

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

	private static long currentTime;
	private static long currentDay;
	private static long currentHours;
	private static long currentMinutes;
	private static long currentSeconds;

	/**
	 * initialize the Scoreboard timer
	 */
	static void initScoreboard()
	{
		// define objective timer
		objective = timer.registerNewObjective("Event", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(OBJECTIVETITLE);

		// delete old timer
		resetTimer();

		// get the time since event start
		getCurrentTime();

		// set points
		objective.getScore(TIMERTITLE).setScore(0);
		updateScoreboardTime();

		// launch Scoreboard repetable task
		runScoreboard();
	}

	/**
	 * Get the time since the event start
	 */
	static void getCurrentTime()
	{
		currentTime = ((System.currentTimeMillis() - TimerConfigurationEditor.time) / 1000);

		currentSeconds = currentTime % 60;
		currentTime /= 60;

		currentMinutes = currentTime % 60;
		currentTime /= 60;

		currentHours = currentTime % 60;
		currentTime /= 60;

		currentDay = currentTime / 24;
	}

	static void updateScoreboardTime()
	{
		objective.getScore("Jours : " + currentDay).setScore(-1);
		objective.getScore("Heures : " + currentHours).setScore(-2);
		objective.getScore("Minutes : " + currentMinutes).setScore(-3);
		objective.getScore("Secondes : " + currentSeconds).setScore(-4);
	}

	/**
	 * reset Timer line
	 */
	static void resetTimer()
	{
		timer.resetScores("Jours : " + currentDay);
		timer.resetScores("Heures : " + currentHours);
		timer.resetScores("Minutes : " + currentMinutes);
		timer.resetScores("Secondes : " + currentSeconds);

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
				resetTimer();
				getCurrentTime();
				updateScoreboardTime();
			}

		}, 20L, 20L);

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

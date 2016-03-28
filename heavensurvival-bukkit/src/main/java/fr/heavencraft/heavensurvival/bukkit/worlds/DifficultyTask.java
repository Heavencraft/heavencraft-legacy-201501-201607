package fr.heavencraft.heavensurvival.bukkit.worlds;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.plugin.java.JavaPlugin;

public class DifficultyTask implements Runnable
{
	private static final int NIGHT_TIME = 12500;
	private boolean isNight = false;

	public DifficultyTask(JavaPlugin plugin)
	{
		Bukkit.getScheduler().runTaskTimer(plugin, this, 1, 1);
	}

	public void run()
	{
		if (!isNight && Bukkit.getWorld("world").getTime() > NIGHT_TIME)
		{
			isNight = true;
			WorldsManager.setDifficulty(Difficulty.HARD);
			Bukkit.broadcastMessage("§6Il fait nuit, difficulté HARD");
		}
		else if (isNight && Bukkit.getWorld("world").getTime() < NIGHT_TIME)
		{
			isNight = false;
			WorldsManager.setDifficulty(Difficulty.EASY);
			Bukkit.broadcastMessage("§6Il fait jour, difficulté EASY");
		}
	}
}
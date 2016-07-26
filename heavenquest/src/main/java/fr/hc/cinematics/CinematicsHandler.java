package fr.hc.cinematics;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.hc.cinematics.bukkitrunable.FirstWakeUpCinematics;
import fr.hc.quest.HeavenQuest;

public class CinematicsHandler
{

	public static void lauchCinematics(Player player, Integer cinematicsIndex, Cinematics cinematics)
			throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException,
			IllegalAccessException
	{
		switch (cinematicsIndex)
		{
			case 0:
				player.sendMessage("Lauch first Wake Up Cinematics.");
				BukkitRunnable firstWakeUpCinematicsRunnable = new FirstWakeUpCinematics(player);
				// firstWakeUpCinematicsRunnable.runTaskTimer(cinematics, 0L, 5L);
				firstWakeUpCinematicsRunnable.runTaskTimer(HeavenQuest.get(), 0L, 5L);

			case 1:
				player.sendMessage("Lauch tutorial Cinematics.");

			default:
				break;
		}
	}

}

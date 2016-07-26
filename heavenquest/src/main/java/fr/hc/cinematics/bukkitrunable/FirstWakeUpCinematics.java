package fr.hc.cinematics.bukkitrunable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.hc.cinematics.components.CinematicsEffects;
import fr.hc.cinematics.components.CinematicsHelper;
import fr.hc.cinematics.components.CinematicsTeleporter;

public class FirstWakeUpCinematics extends BukkitRunnable
{
	private final CommandSender player;
	private Integer timer = 0;
	private final Integer index = 0;

	private final Map<Integer, Method> cinematicsPartition = new HashMap<Integer, Method>();

	public FirstWakeUpCinematics(Player player) throws NoSuchMethodException, SecurityException,
			ClassNotFoundException, InstantiationException, IllegalAccessException
	{
		this.player = player;

		// write cinematics commander. 4u = 1s.
		cinematicsPartition.put(0, CinematicsTeleporter.class.getMethod("teleportWithoutBackUpPoint", new Class[]
		{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(4, CinematicsEffects.class.getMethod("castSimpleEffect", new Class[]
		{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(10,
				CinematicsTeleporter.class.getMethod("teleportWithRelativeCoordinate", new Class[]
				{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(30,
				CinematicsTeleporter.class.getMethod("teleportWithRelativeCoordinate", new Class[]
				{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(30,
				CinematicsTeleporter.class.getMethod("teleportWithRelativeCoordinate", new Class[]
				{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(40,
				CinematicsTeleporter.class.getMethod("teleportWithRelativeCoordinate", new Class[]
				{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(49, CinematicsEffects.class.getMethod("removeEffects", new Class[]
		{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(50, CinematicsTeleporter.class.getMethod("teleportWithoutBackUpPoint", new Class[]
		{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(51, CinematicsHelper.class.getMethod("NPCSpeak", new Class[]
		{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(53, CinematicsEffects.class.getMethod("castSimpleEffect", new Class[]
		{ Player.class, Integer.class, Integer.class }));
		cinematicsPartition.put(55, CinematicsHelper.class.getMethod("NPCSpeak", new Class[]
		{ Player.class, Integer.class, Integer.class }));
	}

	@Override
	public void run()
	{
		player.sendMessage("Timer is : " + timer);
		if (cinematicsPartition.containsKey(timer))
		{
			try
			{
				cinematicsPartition.get(timer).invoke(this, new Object[]
				{ player, timer, index });
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}

		}
		if (timer >= 60)
			this.cancel();

		timer++;

	}

}

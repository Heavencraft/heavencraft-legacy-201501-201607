package fr.heavencraft.heavenevent;

import fr.heavencraft.heavencore.bukkit.listeners.ColoredSignsListener;
import fr.heavencraft.heavenevent.timer.TimerCommands;

public class InitManager
{
	public static void init(HeavenEvent plugin)
	{
		/*
		 * from HeavenCore
		 */

		new ColoredSignsListener(plugin);

		/*
		 * from HeavenEvent
		 */

		new PlayerJoinListener(plugin);
		new TimerCommands(plugin);

	}

}

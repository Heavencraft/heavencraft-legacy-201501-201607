package fr.heavencraft.heavenevent;

import org.bukkit.Bukkit;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavenevent.timer.Timer;

public class HeavenEvent extends HeavenPlugin
{
	public static HeavenEvent _instance;

	public HeavenEvent()
	{
		_instance = this;
	}

	@Override
	public void onEnable()
	{

		try
		{
			super.onEnable();

			InitManager.init(this);

		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}

		saveConfig();

		/**
		 * load Timer
		 */
		Timer.loadTimer();

	}

	@Override
	public void onDisable()
	{
		saveConfig();
	}

	public static HeavenEvent getInstance()
	{
		return _instance;
	}
}

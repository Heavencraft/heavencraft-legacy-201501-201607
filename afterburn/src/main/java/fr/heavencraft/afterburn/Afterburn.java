package fr.heavencraft.afterburn;

import org.bukkit.Bukkit;

import fr.heavencraft.afterburn.flight.PlaneControlsListener;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

public class Afterburn extends HeavenPlugin
{
	private static Afterburn _instance;
	
	@Override
	public void onEnable()
	{
		try
		{
			super.onEnable();
			_instance = this;
		}
		catch (final Exception ex)
		{
			ex.printStackTrace();
			Bukkit.shutdown();
		}
		new PlaneControlsListener(get());
	}

	@Override
	protected void afterEnable()
	{
		super.afterEnable();
	}

	public static Afterburn get()
	{
		return _instance;
	}
	
}

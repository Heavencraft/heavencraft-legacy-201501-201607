package fr.hc.cinematics;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;

import fr.hc.cinematics.components.CinematicsHelper;
import fr.hc.cinematics.components.CinematicsSeeSight;
import fr.hc.cinematics.components.CinematicsTeleporter;

public class Cinematics extends JavaPlugin
{

	/*
	 * Cinematics descriptions
	 */

	private final Map<Integer, String> cinematicsDescriptions = new HashMap<Integer, String>();

	public Cinematics()
	{
		cinematicsDescriptions.put(0, "Introduction, vous vous reveillez pr�s des murailles.");
		cinematicsDescriptions.put(1, "Combat, Initiation au combat.");
	}

	protected boolean isCinematicsAvailable(Integer cinematicsIndex)
	{
		if (cinematicsDescriptions.containsKey(cinematicsIndex))
			return true;

		return false;
	}

	protected String getCinematicsDescription(Integer cinematicsIndex)
	{
		return cinematicsDescriptions.get(cinematicsIndex);
	}

	@Override
	public void onEnable()
	{
		getCommand("Cinematics").setExecutor(new CinematicsCommand(this, cinematicsDescriptions.size()));

		// Initialize cinematics components.
		try
		{
			CinematicsHelper.class.newInstance();
			CinematicsSeeSight.class.newInstance();
			CinematicsTeleporter.class.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

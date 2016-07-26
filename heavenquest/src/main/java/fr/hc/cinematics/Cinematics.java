package fr.hc.cinematics;

import java.util.HashMap;
import java.util.Map;

import fr.hc.cinematics.components.CinematicsEffects;
import fr.hc.cinematics.components.CinematicsHelper;
import fr.hc.cinematics.components.CinematicsTeleporter;
import fr.hc.quest.HeavenQuest;

public class Cinematics
{

	private static Cinematics instance = null;

	public static Cinematics get()
	{
		if (Cinematics.instance == null)
			Cinematics.instance = new Cinematics(HeavenQuest.get());
		return Cinematics.instance;
	}

	/*
	 * Cinematics descriptions
	 */

	private final Map<Integer, String> cinematicsDescriptions = new HashMap<Integer, String>();

	public Cinematics(HeavenQuest plugin)
	{
		cinematicsDescriptions.put(0, "Introduction, vous vous reveillez pr�s des murailles.");
		cinematicsDescriptions.put(1, "Combat, Initiation au combat.");

		// Initialize cinematics components.
		try
		{
			CinematicsHelper.class.newInstance();
			CinematicsEffects.class.newInstance();
			CinematicsTeleporter.class.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
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

	public int getSize()
	{
		return cinematicsDescriptions.size();
	}

}

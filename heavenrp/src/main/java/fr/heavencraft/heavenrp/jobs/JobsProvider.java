package fr.heavencraft.heavenrp.jobs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public abstract class JobsProvider
{
	private static final Map<String, Job> jobsByName = new HashMap<String, Job>();

	public static void loadConfig()
	{
		try (final BufferedReader reader = new BufferedReader(
				new FileReader(HeavenRP.getInstance().getDataFolder().getPath() + "/jobs.cfg")))
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				line = line.trim();

				// Empty line and comments
				if (line.isEmpty() || line.startsWith("#"))
					continue;

				line = line.replaceAll("\\s", ""); // Maybe better than do trim everywhere ?

				createJobFromString(line);
			}
		}
		catch (final FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final IOException e)
		{
			e.printStackTrace();
		}
		catch (final HeavenException e)
		{
			e.printStackTrace();
		}
	}

	// Input format : NAME=[<ACTION>.<MATERIAL>.<POINTS>]*
	public static void createJobFromString(String input) throws HeavenException
	{
		final String[] splitEquals = input.split("=", 2);

		final String name = splitEquals[0];
		System.out.println("Name : " + name);

		final String[] actions = splitEquals[1].split("\\|");

		final Job job = new Job(name);

		for (final String action : actions)
		{
			JobUtil.addActions(action, job);
		}

		jobsByName.put(name, job);
	}

	public static Job getJobByName(String name)
	{
		return jobsByName.get(name);
	}
}
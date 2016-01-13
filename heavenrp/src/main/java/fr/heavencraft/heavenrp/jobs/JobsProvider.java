package fr.heavencraft.heavenrp.jobs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	}

	// Input format : NAME=[<ACTION>.<MATERIAL>.<POINTS>]*
	public static void createJobFromString(String input)
	{
		final String[] splitEquals = input.split("=", 2);

		final String name = splitEquals[0];
		System.out.println("Name : " + name);

		final String[] actions = splitEquals[1].split("\\|");

		final Job job = new Job(name);

		for (final String action : actions)
		{
			final String[] actionParams = action.split("\\.");

			final String code = actionParams[0];

			final JobActionType actionType = JobActionType.getActionTypeByCode(code.charAt(0));
			final JobAction jobAction = new JobAction(actionType,
					actionType.createParamFromString(actionParams[1]));

			final int points = Integer.parseInt(actionParams[2]);

			System.out.println("Add action : " + jobAction + " -> " + points);
			job.addAction(jobAction, points);
		}

		jobsByName.put(name, job);
	}

	public static Job getJobByName(String name)
	{
		return jobsByName.get(name);
	}
}
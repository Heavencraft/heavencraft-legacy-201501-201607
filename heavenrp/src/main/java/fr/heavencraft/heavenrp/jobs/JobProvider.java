package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

public abstract class JobProvider
{
	private static final Map<String, Job> jobsByName = new HashMap<String, Job>();

	// Input format : NAME=[<ACTION>.<MATERIAL>.<POINTS>]*
	public static void createJobFromString(String input)
	{
		final String[] splitEquals = input.split("=", 2);

		final String name = splitEquals[0];
		System.out.println("Name : " + name);

		final String[] actions = splitEquals[1].split("\\|");

		Job job = new Job(name);

		for (final String action : actions)
		{
			System.out.println("Action : " + action);
			final String[] actionParams = action.split("\\.");

			String code = actionParams[0];

			final JobActionType actionType = JobActionType.getActionTypeByCode(code.charAt(0));
			JobAction jobAction = new JobAction(actionType, actionType.createParamFromString(actionParams[1]));

			int points = Integer.parseInt(actionParams[2]);

			job.addAction(jobAction, points);
		}

		jobsByName.put(name, job);
	}

	public static Job getJobByName(String name)
	{
		return jobsByName.get(name);
	}
}
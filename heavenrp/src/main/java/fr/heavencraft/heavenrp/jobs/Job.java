package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

import fr.heavencraft.heavenrp.jobs.actions.JobAction;
import fr.heavencraft.heavenrp.jobs.actions.JobActionType;

public class Job
{
	private static final Map<String, Job> jobsByName = new HashMap<String, Job>();

	// Input format : NAME=[<ACTION>.<MATERIAL>.<POINTS>]*
	public static void createJobFromString(String input)
	{
		final String[] splitEquals = input.split("=", 2);

		final String name = splitEquals[0];
		final String[] actions = splitEquals[1].split("|");

		final Map<JobAction, Integer> pointsByAction = new HashMap<JobAction, Integer>();

		for (final String action : actions)
		{
			final String[] actionParams = action.split(".");
			
			final JobActionType actionType = JobActionType.
		}
	}

	public static Job getUniqueInstanceByName(String name)
	{
		return jobsByName.get(name);
	}

	private final String name;
	private final Map<JobAction, Integer> pointsByAction;

	private Job(String name, Map<JobAction, Integer> pointsByAction)
	{
		this.name = name;
		this.pointsByAction = pointsByAction;
	}

	public int getPointsForAction(JobAction action)
	{
		final Integer points = pointsByAction.get(action);
		return (points != null ? points : 0);
	}
}
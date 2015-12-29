package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Job
{
	private static final Map<String, Job> jobsByName = new HashMap<String, Job>();

	// Input format : NAME=[<ACTION>.<MATERIAL>.<POINTS>]*
	public static void createJobFromString(String input)
	{
		final String[] splitEquals = input.split("=", 2);

		final String name = splitEquals[0];
		System.out.println("Name : " + name);

		final String[] actions = splitEquals[1].split("\\|");

		final Map<JobAction, Integer> pointsByAction = new HashMap<JobAction, Integer>();

		for (final String action : actions)
		{
			System.out.println("Action : " + action);
			final String[] actionParams = action.split("\\.");

			String code = actionParams[0];

			final JobActionType actionType = JobActionType.getActionTypeByCode(code.charAt(0));
			JobAction jobAction = new JobAction(actionType, actionType.createParamFromString(actionParams[1]));

			int points = Integer.parseInt(actionParams[2]);

			pointsByAction.put(jobAction, points);
		}

		jobsByName.put(name, new Job(name, pointsByAction));
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

	@Override
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		str.append("Name : ").append(name).append("\n");
		for (Entry<JobAction, Integer> action : pointsByAction.entrySet())
		{
			str.append("Action : ").append(action.getKey()).append(" => ").append(action.getValue()).append("\n");
		}
		return str.toString();
	}
}
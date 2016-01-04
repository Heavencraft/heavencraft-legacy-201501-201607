package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

public class Job
{
	private final String name;
	private final Map<JobAction, Integer> pointsByAction = new HashMap<JobAction, Integer>();

	/* package */ Job(String name)
	{
		this.name = name;
	}

	/* package */ void addAction(JobAction action, int points)
	{
		pointsByAction.put(action, points);
	}

	public int getPointsForAction(JobAction action)
	{
		final Integer points = pointsByAction.get(action);
		return (points != null ? points : 0);
	}
}
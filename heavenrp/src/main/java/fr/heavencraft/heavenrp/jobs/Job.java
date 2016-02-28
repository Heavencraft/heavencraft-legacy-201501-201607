package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

public class Job
{
	private final int id;
	private final String name;
	private final Map<JobAction, Integer> pointsByAction = new HashMap<JobAction, Integer>();

	/* package */ Job(int id, String name)
	{
		this.id = id;
		this.name = name;
	}

	/* package */ void addAction(JobAction action, int points)
	{
		pointsByAction.put(action, points);
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public int getPointsForAction(JobAction action)
	{
		final Integer points = pointsByAction.get(action);
		return (points != null ? points : 0);
	}
}
package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

public class Job
{
	private static final Map<String, Job> jobsByName = new HashMap<String, Job>();

	// Input format : NAME=[<ACTION>.<MATERIAL>.<POINTS>]*
	public void createJobFromString(String input)
	{

	}

	public static Job getUniqueInstanceByName(String name) {
		return jobsByName.get(name);
	}

	private Job(name, Collection<>)
}
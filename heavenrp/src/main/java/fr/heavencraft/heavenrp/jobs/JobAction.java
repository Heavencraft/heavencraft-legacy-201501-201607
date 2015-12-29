package fr.heavencraft.heavenrp.jobs;

public class JobAction
{
	JobActionType type;
	Object something;

	public JobAction(JobActionType type, Object something)
	{
		this.type = type;
		this.something = something;
	}

	@Override
	public String toString()
	{
		return type.toString() + " " + something.toString();
	}
}
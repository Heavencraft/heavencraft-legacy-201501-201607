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

	@Override
	public boolean equals(Object obj)
	{
		return hashCode() == obj.hashCode();
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (something != null ? something.hashCode() : 0);
		return result;
	}
}
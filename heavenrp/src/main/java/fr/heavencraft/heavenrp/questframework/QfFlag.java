package fr.heavencraft.heavenrp.questframework;

/**
 * This class allows to create flags, which can be added to a PlayerContext or QuestContext
 * @author Manuel
 *
 */
public class QfFlag
{

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flagKey == null) ? 0 : flagKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QfFlag other = (QfFlag) obj;
		if (flagKey == null)
		{
			if (other.flagKey != null)
				return false;
		}
		else if (!flagKey.equals(other.flagKey))
			return false;
		return true;
	}

	private final String flagKey;
	
	/**
	 * Creates a new flag
	 * @param flagIdentifier Unique Identifier used to reference this flag in a context store.
	 */
	public QfFlag(String flagIdentifier)
	{
		this.flagKey = flagIdentifier;
	}
	
	/**
	 * Returns the key used to reference the flag in a context.
	 * @return
	 */
	public String getKey()
	{
		return flagKey;
	}

}

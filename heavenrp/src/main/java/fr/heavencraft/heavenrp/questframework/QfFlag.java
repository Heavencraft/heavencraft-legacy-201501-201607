package fr.heavencraft.heavenrp.questframework;

import fr.heavencraft.heavenrp.exceptions.QuestFlagTypeException;

/**
 * This class allows to create flags, which can be added to a PlayerContext or QuestContext
 * @author Manuel
 *
 */
public class QfFlag
{
	
	/**
	 * Returns the Data Type Index used to reference a type in database
	 * @param object
	 * @return
	 * @throws QuestFlagTypeException 
	 */
	public static int getDataIndexFromObject(Object object) throws QuestFlagTypeException
	{
		if(object instanceof Integer)
			return 0;
		if(object instanceof Boolean)
			return 1;
		if(object instanceof String)
			return 2;
		throw new QuestFlagTypeException(object);
	}
	
	/**
	 * Takes an incoming Object, and parses it to the right type
	 * @param object content
	 * @param dataTypeIndex index of data type
	 * @return
	 * @throws QuestFlagTypeException 
	 */
	public static Object parseToTypeByIndex(String object, int dataTypeIndex) throws QuestFlagTypeException
	{
		if(dataTypeIndex == 0)
			return Integer.parseInt(object);
		if(dataTypeIndex == 1)
			return Boolean.parseBoolean(object);
		if(dataTypeIndex == 2)
			return object;
		throw new QuestFlagTypeException(dataTypeIndex);
	}

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

package fr.heavencraft.heavenrp.questframework;

/**
 * This class allows to create flags, which can be added to a PlayerContext or QuestContext
 * @author Manuel
 *
 */
public class QfFlag
{

	private final String flagId;
	
	/**
	 * Creates a new flag
	 * @param flagId Unique Identifier used to reference this flag in a context store.
	 */
	public QfFlag(String flagId)
	{
		this.flagId = flagId;
	}
	
	/**
	 * Returns the key used to reference the flag in a context.
	 * @return
	 */
	public String getKey()
	{
		return flagId;
	}

}

package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.questframework.QfFlag;

public class QuestFlagKeyTooLongException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	/**
	 * The flag uses a key that is larger than the allowed size in database
	 * 
	 * @param flag
	 */
	public QuestFlagKeyTooLongException(QfFlag flag)
	{
		super("Flag key '%1$s' is too long, and is not storeable in database.", flag.getKey());
	}

}

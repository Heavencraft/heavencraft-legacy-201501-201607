package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.questframework.QfFlag;

/**
 * This exception should be thrown when a QuestFlag is added to a context, but there is already a flag with this name.
 * 
 * @author Manuel
 *
 */
public class QuestFlagTypeException extends HeavenException
{

	private static final long serialVersionUID = 1L;

	/**
	 * A flag with the same name already exist in this context
	 * 
	 * @param flag
	 */
	public QuestFlagTypeException(QfFlag flag)
	{
		super("Erreur de mise a jour de flag: {%1$s} n'as pas le droit de changer de type.",
				flag.getKey());
	}
}

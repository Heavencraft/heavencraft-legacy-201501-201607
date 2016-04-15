package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.questframework.QfFlag;

/**
 * This exception should be thrown when a QuestFlag is added to a context,
 * but there is already a flag with this name.
 * @author Manuel
 *
 */
public class QuestFlagCollisionException extends HeavenException
{

	private static final long serialVersionUID = 1L;

	/**
	 * A flag with the same name already exist in this context
	 * 
	 * @param flag
	 */
	public QuestFlagCollisionException(QfFlag flag)
	{
		super("Collision de flag détéctée {%1$s} existe déjà dans ce contexte.", flag.getKey());
	}
}

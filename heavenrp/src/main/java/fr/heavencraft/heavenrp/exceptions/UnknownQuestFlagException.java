package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.questframework.QfFlag;

/**
 * 
 * @author Manuel
 *
 */
public class UnknownQuestFlagException extends HeavenException
{

	private static final long serialVersionUID = 1L;

	public UnknownQuestFlagException(QfFlag flag)
	{
		super("Flag inconnu détéctée {%1$s} n'existe pas dans ce contexte.", flag.getKey());
	}
}

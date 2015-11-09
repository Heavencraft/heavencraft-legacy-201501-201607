package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class UnknownErrorException extends HeavenException {

	private static final long serialVersionUID = 1L;

	public UnknownErrorException()
	{
		super("Cette erreur n'est pas censée se produire. Veuillez contacter un administrateur");
	}
}

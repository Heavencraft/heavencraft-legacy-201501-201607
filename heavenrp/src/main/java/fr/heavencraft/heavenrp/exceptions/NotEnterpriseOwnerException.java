package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.exceptions.HeavenException;

public class NotEnterpriseOwnerException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public NotEnterpriseOwnerException(String name)
	{
		super("Vous n'êtes pas propriétaire de l'entreprise {%1$s}", name);
	}
}
package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class EnterpriseNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public EnterpriseNotFoundException(String name)
	{
		super("L'entreprise {%1$s} n'existe pas.", name);
	}
}
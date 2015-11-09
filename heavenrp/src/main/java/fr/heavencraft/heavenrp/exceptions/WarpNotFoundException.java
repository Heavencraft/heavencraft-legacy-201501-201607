package fr.heavencraft.heavenrp.exceptions;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public class WarpNotFoundException extends HeavenException {

	private static final long serialVersionUID = 1L;

	public WarpNotFoundException(String name)
	{
		super("La warp {%1$s} n'existe pas.", name);
	}
}

package fr.heavencraft.heavensurvival.bukkit.teleport;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.Region;

public class NotOwnerException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public NotOwnerException(Region region)
	{
		super("Vous n'êtes pas le propriétaire de la protection {%1$s}.", region.getName());
	}
}
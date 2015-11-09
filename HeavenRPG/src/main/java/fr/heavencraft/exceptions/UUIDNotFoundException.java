package fr.heavencraft.exceptions;

public class UUIDNotFoundException extends HeavenException
{
	private static final long serialVersionUID = 1L;

	public UUIDNotFoundException(String name)
	{
		super("Le joueur {%1$s} n'a pas d'UUID, vous avez sûrement fait une faute de frappe.", name);
	}
}
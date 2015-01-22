package fr.heavencraft.heavenguard.api;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class Flag
{
	private static Map<String, Flag> flagsByName = new HashMap<String, Flag>();

	public static final Flag PVP = new Flag("pvp", Types.BIT);
	public static final Flag PUBLIC = new Flag("public", Types.BIT);

	public static Flag getUniqueInstanceByName(String name)
	{
		return flagsByName.get(name.toLowerCase());
	}

	private final String name;
	private final int type;

	Flag(String name, int type)
	{
		this.name = name;
		this.type = type;

		flagsByName.put(name, this);
	}

	public String getName()
	{
		return name;
	}

	public int getType()
	{
		return type;
	}
}
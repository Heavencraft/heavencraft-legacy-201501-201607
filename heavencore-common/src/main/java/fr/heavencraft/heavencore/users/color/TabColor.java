package fr.heavencraft.heavencore.users.color;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TabColor
{
	private static final Map<String, TabColor> instancesByCode = new HashMap<String, TabColor>();

	public static final TabColor BLACK = new TabColor("0", "black");
	public static final TabColor DARK_BLUE = new TabColor("1", "dark_blue");
	public static final TabColor DARK_GREEN = new TabColor("2", "dark_green");
	public static final TabColor DARK_AQUA = new TabColor("3", "dark_aqua");
	public static final TabColor DARK_RED = new TabColor("4", "dark_red");
	public static final TabColor DARK_PURPLE = new TabColor("5", "dark_purple");
	public static final TabColor GOLD = new TabColor("6", "gold");
	public static final TabColor GRAY = new TabColor("7", "gray");
	public static final TabColor DARK_GRAY = new TabColor("8", "dark_gray");
	public static final TabColor BLUE = new TabColor("9", "blue");
	public static final TabColor GREEN = new TabColor("a", "green");
	public static final TabColor AQUA = new TabColor("b", "aqua");
	public static final TabColor RED = new TabColor("c", "red");
	public static final TabColor LIGHT_PURPLE = new TabColor("d", "light_purple");
	public static final TabColor YELLOW = new TabColor("e", "yellow");
	public static final TabColor WHITE = new TabColor("f", "white");

	public static TabColor getUniqueInstanceByCode(String code)
	{
		return instancesByCode.get(code);
	}

	public static Collection<TabColor> getAllColors()
	{
		return instancesByCode.values();
	}

	private final String code;
	private final String name;

	private TabColor(String code, String name)
	{
		this.code = code;
		this.name = name;

		instancesByCode.put(code, this);
	}

	public String getCode()
	{
		return code;
	}

	public String getName()
	{
		return name;
	}
}
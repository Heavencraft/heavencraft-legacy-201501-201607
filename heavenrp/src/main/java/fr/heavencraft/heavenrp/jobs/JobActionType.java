package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public enum JobActionType
{
	BAKE('S', Material.class), // Cuire
	BREAK('B', Material.class), // Casser
	CRAFT('C', Material.class), // Crafter
	KILL('K', EntityType.class), // Kill
	FISH('P', EntityType.class); // Pécher

	private final char code;
	private final Class<?> actionParam;

	private JobActionType(char code, Class<?> actionParam)
	{
		this.code = code;
		this.actionParam = actionParam;
	}

	public Object createParamFromString(String input)
	{
		if (actionParam == EntityType.class)
			return EntityType.fromName(input);
		else if (actionParam == Material.class)
			return Material.getMaterial(input);
		else
			return null;
	}

	private static final Map<Character, JobActionType> actionTypeByCode = new HashMap<Character, JobActionType>();

	static
	{
		for (JobActionType actionType : values())
			actionTypeByCode.put(actionType.code, actionType);
	}

	public static JobActionType getActionTypeByCode(char code)
	{
		return actionTypeByCode.get(code);
	}
}
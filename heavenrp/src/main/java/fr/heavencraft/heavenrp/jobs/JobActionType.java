package fr.heavencraft.heavenrp.jobs;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.EntityType;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public enum JobActionType
{
	BAKE('S', BlockType.class), // Cuire
	BREAK('B', BlockType.class), // Casser
	CRAFT('C', BlockType.class), // Crafter
	KILL('K', EntityType.class), // Kill
	FISH('P', BlockType.class); // Pécher

	private final char code;
	private final Class<?> expectedParamClass;

	private JobActionType(char code, Class<?> expectedParamClass)
	{
		this.code = code;
		this.expectedParamClass = expectedParamClass;
	}

	public Class<?> getExpectedParamClass()
	{
		return expectedParamClass;
	}

	private static final Map<Character, JobActionType> actionTypeByCode = new HashMap<Character, JobActionType>();

	static
	{
		for (final JobActionType actionType : values())
			actionTypeByCode.put(actionType.code, actionType);
	}

	public static JobActionType getActionTypeByCode(char code) throws HeavenException
	{
		final JobActionType actionType = actionTypeByCode.get(code);

		if (actionType == null)
			throw new HeavenException("No JobActionType found for code '%1$s'", code);

		return actionType;
	}
}
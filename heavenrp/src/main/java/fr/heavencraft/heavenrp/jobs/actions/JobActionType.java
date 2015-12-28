package fr.heavencraft.heavenrp.jobs.actions;

import java.util.HashMap;
import java.util.Map;

public enum JobActionType
{
	BAKE('S'), // Cuire
	BREAK('B'), // Casser
	CRAFT('C'), // Crafter
	KILL('K'), // Kill
	FISH('P'); // Pécher

	private static final Map<Character, JobActionType> actionTypeById = new HashMap<Character, JobActionType>();

	private JobActionType(char id)
	{
		actionTypeById.put(id, this);
	}
}
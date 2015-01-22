package fr.heavencraft.heavenguard.api;

import java.util.Map;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public interface HasBooleanFlags
{
	Map<Flag, Boolean> getBooleanFlags();

	Boolean getBooleanFlag(Flag flag);

	void setBooleanFlag(Flag flag, Boolean value) throws HeavenException;
}
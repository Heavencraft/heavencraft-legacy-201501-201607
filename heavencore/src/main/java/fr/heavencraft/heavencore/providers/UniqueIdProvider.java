package fr.heavencraft.heavencore.providers;

import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.UserNotFoundException;

public interface UniqueIdProvider extends Provider
{
	String getNameFromUniqueId(UUID uuid) throws UserNotFoundException;
}
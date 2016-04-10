package fr.heavencraft.deprecated;

import java.util.Collection;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.providers.Provider;

public interface DeprecatedUserProvider<U extends DeprecatedUser> extends Provider
{
	U createUser(UUID uuid, String name) throws HeavenException;

	U getUserByUniqueId(UUID uuid) throws UserNotFoundException;

	U getUserByName(String name) throws UserNotFoundException;

	Collection<U> getAllUsers();
}
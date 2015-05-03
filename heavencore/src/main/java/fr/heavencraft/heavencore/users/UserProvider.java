package fr.heavencraft.heavencore.users;

import java.util.Collection;
import java.util.UUID;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.providers.Provider;

public interface UserProvider<U extends User> extends Provider
{
	U createUser(UUID uuid, String name) throws HeavenException;

	U getUserByUniqueId(UUID uuid) throws UserNotFoundException;

	U getUserByName(String name) throws UserNotFoundException;

	Collection<U> getAllUsers();
}
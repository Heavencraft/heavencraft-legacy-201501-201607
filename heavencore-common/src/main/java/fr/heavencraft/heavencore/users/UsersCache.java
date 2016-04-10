package fr.heavencraft.heavencore.users;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Only accessible from package
class UsersCache<U extends User>
{
	private final Map<UUID, U> usersByUniqueId = new ConcurrentHashMap<UUID, U>();
	private final Map<String, U> usersByName = new ConcurrentHashMap<String, U>();

	public U getUserByUniqueId(UUID uniqueId)
	{
		return usersByUniqueId.get(uniqueId);
	}

	public U getUserByName(String name)
	{
		return usersByName.get(name);
	}

	public void addToCache(U user)
	{
		usersByUniqueId.put(user.getUniqueId(), user);
		usersByName.put(user.getName(), user);
	}

	public void invalidateCache(U user)
	{
		System.out.println("Invalidate cache: User [ " + user.getName() + "]");

		usersByUniqueId.remove(user.getUniqueId());
		usersByName.remove(user.getName());
	}
}
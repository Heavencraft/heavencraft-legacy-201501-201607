package fr.heavencraft.heavensurvival.common.users;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

// Only accessible from package
class UsersCache
{
	private final Map<UUID, User> usersByUniqueId = new ConcurrentHashMap<UUID, User>();
	private final Map<String, User> usersByName = new ConcurrentHashMap<String, User>();

	public User getUserByUniqueId(UUID uniqueId)
	{
		return usersByUniqueId.get(uniqueId);
	}

	public User getUserByName(String name)
	{
		return usersByName.get(name);
	}

	public void addToCache(User user)
	{
		usersByUniqueId.put(user.getUniqueId(), user);
		usersByName.put(user.getName(), user);
	}

	public void invalidateCache(User user)
	{
		System.out.println("Invalidate cache: User [ " + user.getName() + "]");

		usersByUniqueId.remove(user.getUniqueId());
		usersByName.remove(user.getName());
	}
}
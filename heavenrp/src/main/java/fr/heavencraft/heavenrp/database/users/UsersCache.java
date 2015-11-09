package fr.heavencraft.heavenrp.database.users;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Only accessible from package
class UsersCache
{
	private static final Map<String, User> usersByUUID = new ConcurrentHashMap<String, User>();
	private static final Map<String, User> usersByName = new ConcurrentHashMap<String, User>();

	public static User getUserByUUID(String uuid)
	{
		return usersByUUID.get(uuid);
	}

	public static User getUserByName(String name)
	{
		return usersByName.get(name);
	}

	public static void addToCache(User user)
	{
		usersByUUID.put(user.getUUID(), user);
		usersByName.put(user.getName(), user);
	}

	public static void invalidateCache(User user)
	{

		System.out.println("Invalidate cache: User [ " + user.getName() + "]");

		usersByUUID.remove(user.getUUID());
		usersByName.remove(user.getName());
	}
}
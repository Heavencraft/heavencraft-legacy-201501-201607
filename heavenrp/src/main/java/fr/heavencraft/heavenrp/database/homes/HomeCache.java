package fr.heavencraft.heavenrp.database.homes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;

import fr.heavencraft.heavenrp.database.users.User;

// Only accessible from package
final class HomeCache
{
	private static final Map<Integer, Map<Integer, Location>> cache = new ConcurrentHashMap<Integer, Map<Integer, Location>>();

	public static Location getHome(User user, int nb)
	{
		Map<Integer, Location> homes = cache.get(user.getId());

		if (homes == null)
			return null;

		return homes.get(nb);
	}

	public static void addToCache(User user, int nb, Location home)
	{
		Map<Integer, Location> homes = cache.get(user.getId());

		if (homes == null)
		{
			homes = new ConcurrentHashMap<Integer, Location>();
			cache.put(user.getId(), homes);
		}

		homes.put(nb, home);
	}

	public static void invalidateCache(User user, int nb)
	{

		Map<Integer, Location> homes = cache.get(user.getId());

		if (homes == null)
			return;

		System.out.println("Invalidate cache: Home [ " + user.getName() + ", " + nb + "]");
		homes.remove(nb);
	}

}
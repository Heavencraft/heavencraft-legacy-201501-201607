package fr.heavencraft.heavenfun.common.users;

import fr.heavencraft.heavencore.users.UserProvider;
import fr.heavencraft.heavenfun.common.HeavenFunInstance;

public class FunUserProvider extends UserProvider<FunUser>
{
	protected FunUserProvider()
	{
		super(HeavenFunInstance.get().getConnectionProvider(), new FunUserFactory());
	}

	/*
	 * Singleton pattern
	 */

	private static FunUserProvider instance;

	public static FunUserProvider get()
	{
		if (instance == null)
			instance = new FunUserProvider();

		return instance;
	}
}
package fr.heavencraft.heavensurvival.common.users;

import fr.heavencraft.heavencore.users.UserProvider;
import fr.heavencraft.heavensurvival.common.HeavenSurvivalInstance;

public class SurvivalUserProvider extends UserProvider<SurvivalUser>
{
	private SurvivalUserProvider()
	{
		super(HeavenSurvivalInstance.get().getConnectionProvider(), new SurvivalUserFactory());
	}

	/*
	 * Singleton pattern
	 */

	private static SurvivalUserProvider instance;

	public static SurvivalUserProvider get()
	{
		if (instance == null)
			instance = new SurvivalUserProvider();

		return instance;
	}
}
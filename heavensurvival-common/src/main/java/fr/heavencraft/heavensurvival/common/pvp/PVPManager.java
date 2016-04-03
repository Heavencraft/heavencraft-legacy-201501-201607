package fr.heavencraft.heavensurvival.common.pvp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavensurvival.common.users.UpdateUserPvpQuery;
import fr.heavencraft.heavensurvival.common.users.User;

public class PVPManager
{
	private static final long CHANGE_INTERVAL = 300000; // 5 minutes

	private final Map<UUID, Long> lastChangeByPlayer = new HashMap<UUID, Long>();

	private PVPManager()
	{
	}

	public void setEnabled(User user, boolean enabled) throws HeavenException
	{
		final long currentTime = System.currentTimeMillis();
		final Long lastChange = lastChangeByPlayer.get(user.getUniqueId());

		if (lastChange != null && (currentTime - lastChange) < CHANGE_INTERVAL)
			throw new HeavenException("Vous ne pouvez pas %1$s le PVP pour le moment.",
					enabled ? "activer" : "désactiver");

		QueriesHandler.addQuery(new UpdateUserPvpQuery(user, enabled)
		{
			@Override
			public void onSuccess()
			{
				lastChangeByPlayer.put(user.getUniqueId(), currentTime);
			}
		});
	}

	/*
	 * Singleton pattern
	 */

	private static final PVPManager instance = new PVPManager();

	public static PVPManager get()
	{
		return instance;
	}
}
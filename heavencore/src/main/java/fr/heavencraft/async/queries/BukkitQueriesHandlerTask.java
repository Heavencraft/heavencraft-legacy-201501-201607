package fr.heavencraft.async.queries;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.CorePlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class BukkitQueriesHandlerTask extends BukkitRunnable
{
	public BukkitQueriesHandlerTask()
	{
		runTaskTimerAsynchronously(CorePlugin.getInstance(), 1L, 1L);
	}

	@Override
	public void run()
	{
		Query tmp;

		while ((tmp = QueriesHandler.pollQuery()) != null)
		{
			final Query query = tmp; // Gruge final

			try
			{
				query.executeQuery();

				Bukkit.getScheduler().runTask(CorePlugin.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						query.onSuccess();
					}
				});
			}
			catch (final HeavenException ex)
			{
				Bukkit.getScheduler().runTask(CorePlugin.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						query.onHeavenException(ex);
					}
				});
			}
			catch (final SQLException ex)
			{
				ex.printStackTrace();

				Bukkit.getScheduler().runTask(CorePlugin.getInstance(), new Runnable()
				{
					@Override
					public void run()
					{
						query.onSQLException(ex);
					}
				});
			}
		}
	}
}
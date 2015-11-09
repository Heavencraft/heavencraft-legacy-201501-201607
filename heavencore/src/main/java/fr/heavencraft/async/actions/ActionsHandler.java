package fr.heavencraft.async.actions;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.CorePlugin;

public class ActionsHandler extends BukkitRunnable
{
	private static Queue<Action> actions = new ConcurrentLinkedQueue<Action>();

	public ActionsHandler()
	{
		runTaskTimer(CorePlugin.getInstance(), 1L, 1L);
	}

	@Override
	public void run()
	{
		if (!actions.isEmpty())
		{
			Action action;

			while ((action = actions.poll()) != null)
			{
				try
				{
					action.executeAction();
					action.onSuccess();
				}
				catch (Exception ex)
				{
					action.onFailure();
				}
			}
		}
	}

	public static void addAction(Action action)
	{
		actions.add(action);
	}
}
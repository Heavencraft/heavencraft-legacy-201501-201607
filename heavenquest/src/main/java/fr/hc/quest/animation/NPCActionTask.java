package fr.hc.quest.animation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.scheduler.BukkitRunnable;

import fr.hc.quest.HeavenQuest;

public class NPCActionTask extends BukkitRunnable
{
	private final Map<Integer, Collection<NPCAction>> actionsToPerform = new HashMap<Integer, Collection<NPCAction>>();
	private int currentTick = 0;

	private NPCActionTask()
	{
		// Run this task every tick
		runTaskTimer(HeavenQuest.get(), 1, 1);
	}

	public void add(int relativeTick, NPCAction action)
	{
		final int tick = currentTick + relativeTick;

		Collection<NPCAction> actions = actionsToPerform.get(tick);
		if (actions == null)
			actionsToPerform.put(tick, actions = new ArrayList<NPCAction>());

		actions.add(action);
	}

	@Override
	public void run()
	{
		currentTick++;

		final Collection<NPCAction> actions = actionsToPerform.get(currentTick);
		if (actions == null)
			return;

		for (final NPCAction action : actions)
			action.perform();
	}

	/*
	 * Singleton pattern
	 */

	private static NPCActionTask instance;

	public static NPCActionTask get()
	{
		if (instance == null)
			instance = new NPCActionTask();

		return instance;
	}
}
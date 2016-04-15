package fr.heavencraft.heavenrp.questframework;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.HeavenRP;

/**
 * This listener handles loading and unloading quests for online players
 * @author Manuel
 *
 */
public class QuestGeneralListener extends AbstractListener<HeavenPlugin>
{
	public QuestGeneralListener(HeavenRP plugin)
	{
		super(plugin);
	}

	// TODO Load quest & context on player join

	// TODO Unload quest & context on player disconnect

}

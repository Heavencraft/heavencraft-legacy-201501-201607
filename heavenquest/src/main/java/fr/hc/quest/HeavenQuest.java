package fr.hc.quest;

import java.util.Iterator;

import fr.hc.scrolls.ScrollCommand;
import fr.hc.scrolls.ScrollListener;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;

public class HeavenQuest extends HeavenPlugin
{
	private static HeavenQuest instance;

	public HeavenQuest()
	{
		instance = this;
	}

	/**
	 * Returns an instance of Heaven Quest
	 * 
	 * @return
	 */
	public static HeavenQuest get()
	{
		return instance;
	}

	@Override
	public void onEnable()
	{
		super.onEnable();

		CitizensAPI.getNPCRegistry().deregisterAll();

		final Iterator<NPC> it = CitizensAPI.getNPCRegistry().iterator();
		int nbRemoved = 0;
		while (it.hasNext())
		{
			it.next().destroy();
			nbRemoved++;
		}
		log.info("%1$s NPC removed", nbRemoved);

		new CreateNPCommand(this);
		new TestCommand(this);
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcListener.class).withName("NpcListener"));

		new ScrollCommand(this);
		new ScrollListener(this);
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}
}

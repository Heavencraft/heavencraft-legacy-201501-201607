package fr.hc.quest;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import fr.hc.cinematics.Cinematics;
import fr.hc.cinematics.CinematicsCommand;
import fr.hc.quest.commands.CreateNPCommand;
import fr.hc.quest.commands.RemoveNPCCommand;
import fr.hc.quest.commands.TestCommand;
import fr.hc.quest.npc.borderpatrol.BorderPatrolListener;
import fr.hc.scrolls.ScrollCommand;
import fr.hc.scrolls.ScrollListener;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.RedstoneLampListener;
import net.citizensnpcs.api.CitizensAPI;
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

		new CreateNPCommand(this);
		new TestCommand(this);
		new RemoveNPCCommand();
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcListener.class).withName("NpcListener"));

		new ScrollCommand(this);
		new ScrollListener(this);
		
		new RedstoneLampListener(this);
		
		// Initialize cinematics Command.
		new CinematicsCommand(Cinematics.get(), HeavenQuest.get());

	}
	
	@Override
	protected void afterEnable()
	{
		super.afterEnable();
		new BorderPatrolListener();
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
		Bukkit.getLogger().log(Level.INFO, "-- Removing Border Patrol NPC's...");
		for(int i = 0; i < BorderPatrolListener.getPatrolList().size(); i++)
		{
			BorderPatrolListener.getPatrolList().get(i).remove();
		}
	}
}

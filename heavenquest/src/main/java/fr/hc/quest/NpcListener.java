package fr.hc.quest;

import org.bukkit.event.EventHandler;

import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;

@TraitName(value = "NpcListener")
public class NpcListener extends Trait
{

	public NpcListener()
	{
		super("NpcListener");
	}

	@EventHandler(ignoreCancelled = true)
	public void onNpcClick(NPCRightClickEvent event)
	{
		ChatUtil.sendMessage(event.getClicker(), "Salut, moi c'est Bobby!");
		event.setCancelled(true);
	}

	@Override
	public void onAttach()
	{
		super.onAttach();
		System.out.println("Coucou les couyous!");
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		super.run();
	}

}

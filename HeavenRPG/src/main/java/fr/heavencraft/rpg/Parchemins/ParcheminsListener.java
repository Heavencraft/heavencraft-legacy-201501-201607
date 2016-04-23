package fr.heavencraft.rpg.Parchemins;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.player.RPGPlayerManager;

public class ParcheminsListener implements Listener {

	public ParcheminsListener()
	{
		//Bukkit.getPluginManager().registerEvents(this, HeavenRPG.getInstance());
	}
	private final static String REQUIREMENT_NOT_MET = "Vous n'avez pas les aptitudes a utiliser cet objet.";

	@EventHandler
	public void onParcheminUse(PlayerInteractEvent event)
	{
		if(event.getPlayer().getItemInHand().getType() != Material.PAPER)
			return;
		
		IParchemin p = ParcheminProvider.getParcheminByItem(event.getPlayer().getItemInHand());
		if(p == null)
			return;
		
		//TODO check if player is in dungeon

		if(p.canDo(RPGPlayerManager.getRPGPlayer(event.getPlayer())))
		{
			p.executeParchemin(RPGPlayerManager.getRPGPlayer(event.getPlayer()));

			ItemStack inHand = event.getPlayer().getItemInHand();
			inHand.setAmount(inHand.getAmount() -1);
			event.getPlayer().setItemInHand(inHand);

			event.setCancelled(true);
		}
		else
			ChatUtil.sendMessage(event.getPlayer(), REQUIREMENT_NOT_MET);

	}
}

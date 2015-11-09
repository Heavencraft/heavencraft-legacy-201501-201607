package fr.lorgan17.heavenrp.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.TrapDoor;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class LampadaireListener extends AbstractListener<HeavenPlugin>
{
	public LampadaireListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		Block block = event.getClickedBlock();

		if (block.getType() != Material.TRAP_DOOR)
			return;

		TrapDoor trapDoor = new TrapDoor(Material.TRAP_DOOR, block.getData());

		if (!trapDoor.isOpen())
			return;

		Block attachedBlock = block.getRelative(trapDoor.getAttachedFace());

		if (attachedBlock.getType() != Material.GLOWSTONE && attachedBlock.getType() != Material.REDSTONE_LAMP_ON)
			return;

		ChatUtil.sendMessage(event.getPlayer(), "Touches pas à {MON} lampadaire, naméoh !");
		event.setCancelled(true);
	}
}
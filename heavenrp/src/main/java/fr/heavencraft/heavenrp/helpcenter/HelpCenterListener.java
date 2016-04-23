package fr.heavencraft.heavenrp.helpcenter;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;

public class HelpCenterListener extends AbstractListener<HeavenPlugin>
{
	private final HelpCenterMessageProvider helpCenter = new HelpCenterMessageProvider();
	public HelpCenterListener(HeavenPlugin plugin)
	{
		super(plugin);
	}

	private static final String MHF_QUESTION = "MHF_Question";
	private static final String TAG = ChatColor.AQUA + "[Infos]";

	private static final String ERROR_MSG = ChatColor.RED + "Informations non disponible (Contacter un Admin)";
	private static final String HEADER_MSG = ChatColor.AQUA + "¤¤¤¤¤¤¤¤¤¤ Infos ¤¤¤¤¤¤¤¤¤¤";
	private static final String FOOTER_MSG = ChatColor.AQUA + "¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤¤";

	@EventHandler(ignoreCancelled = true)
	private void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;

		final Block clickedBlock = event.getClickedBlock();

		switch (clickedBlock.getType())
		{
			case SKULL:
				skullClickHandler((Skull) clickedBlock.getState(), event.getPlayer());
				break;
			case WALL_SIGN:
				signClickHandler((Sign) clickedBlock.getState(), event.getPlayer());
				break;
			default:
				break;
		}
	}

	private void skullClickHandler(Skull skull, Player player)
	{
		if (!MHF_QUESTION.equals(skull.getOwner()))
			return;

		final Block sign = skull.getBlock().getRelative(BlockFace.DOWN).getRelative(skull.getRotation());

		if (sign.getType() != Material.WALL_SIGN)
			return;

		signClickHandler((Sign) sign.getState(), player);
	}

	private void signClickHandler(Sign sign, Player player)
	{
		if (!TAG.equals(sign.getLine(0)))
			return;

		final String message = helpCenter.getMessageByName(sign.getLine(3));

		if (message == null)
			player.sendMessage(ERROR_MSG);
		else
		{
			player.sendMessage(HEADER_MSG);
			player.sendMessage(message);
			player.sendMessage(FOOTER_MSG);
		}
	}

}

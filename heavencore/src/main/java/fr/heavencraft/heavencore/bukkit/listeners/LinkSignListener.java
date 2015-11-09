package fr.heavencraft.heavencore.bukkit.listeners;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class LinkSignListener extends AbstractSignListener
{
	public LinkSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Lien", CorePermissions.LINK_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event)
	{
		return true;
	}

	@Override
	protected void onSignClick(Player player, Sign sign) throws HeavenException
	{
		ChatUtil.sendMessage(player, "Cliquez le lien suivant :");
		ChatUtil.sendMessage(player, "{http://" + sign.getLine(1) + "}");
	}
}
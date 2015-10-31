package fr.heavencraft.heavenrp.warps;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.actions.TeleportPlayerAction;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.utils.ChatUtil;

public class WarpSignListener extends AbstractSignListener
{
	public WarpSignListener(HeavenPlugin plugin)
	{
		super(plugin, "Warp", RPPermissions.WARP_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event) throws HeavenException
	{
		WarpsManager.getWarp(event.getLine(1));
		return true;
	}

	@Override
	protected void onSignClick(final Player player, Sign sign) throws HeavenException
	{
		final String name = sign.getLine(1);

		ActionsHandler.addAction(new TeleportPlayerAction(player, WarpsManager.getWarp(name).getLocation())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "Vous avez été téléporté à {%1$s}.", name);
			}
		});
	}

	@Override
	protected void onSignBreak(Player player, Sign sign) throws HeavenException
	{
	}
}
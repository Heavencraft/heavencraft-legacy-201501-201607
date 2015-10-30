package fr.heavencraft.heavenrp.warps;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.actions.TeleportPlayerAction;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.listeners.sign.SignListener;
import fr.heavencraft.utils.ChatUtil;

public class WarpSignListener extends SignListener
{
	public WarpSignListener()
	{
		super("Warp", RPPermissions.WARP_SIGN);
	}

	@Override
	protected boolean onSignPlace(Player player, SignChangeEvent event)
	{
		try
		{
			WarpsManager.getWarp(event.getLine(1));
			return true;
		}
		catch (HeavenException ex)
		{
			ChatUtil.sendMessage(player, ex.getMessage());
			return false;
		}
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
}
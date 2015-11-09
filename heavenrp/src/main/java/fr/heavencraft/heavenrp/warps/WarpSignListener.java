package fr.heavencraft.heavenrp.warps;

import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractSignListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenrp.RPPermissions;

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
		Location location = WarpsManager.getWarp(name).getLocation();

		PlayerUtil.teleportPlayer(player, location, "Vous avez été téléporté à {%1$s}.", name);
	}
}
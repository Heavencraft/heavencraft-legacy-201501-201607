package fr.heavencraft.heavenguard.bukkit.commands.owner;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.UserNotFoundException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;

public class InfoSubCommand extends AbstractOwnerSubCommand
{
	public InfoSubCommand(BukkitHeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.INFO_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		info(sender, regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/rg {info} <protection>");
	}

	private void info(CommandSender sender, String name) throws HeavenException, UserNotFoundException
	{
		final Region region = plugin.getRegionProvider().getRegionByName(name);

		ChatUtil.sendMessage(sender, "Protection : %1$s", region.getName());
		ChatUtil.sendMessage(sender, "Coordonnées : [{%1$s %2$s %3$s}] -> [{%4$s %5$s %6$s}] ({%7$s})", //
				region.getMinX(), region.getMinY(), region.getMinZ(), //
				region.getMaxX(), region.getMaxY(), region.getMaxZ(), //
				region.getWorld());

		/*
		 * Flags
		 */

		final String flags = region.getFlagHandler().toString();
		if (!flags.isEmpty())
			ChatUtil.sendMessage(sender, flags);

		final Region parent = region.getParent();
		if (parent != null)
			ChatUtil.sendMessage(sender, "Parent : %1$s", parent.getName());

		final Collection<UUID> owners = region.getMembers(true);
		if (!owners.isEmpty())
		{
			final StringBuilder str = new StringBuilder();

			for (final Iterator<UUID> it = owners.iterator(); it.hasNext();)
			{
				str.append(plugin.getUniqueIdProvider().getNameFromUniqueId(it.next()));

				if (it.hasNext())
					str.append(", ");
			}

			ChatUtil.sendMessage(sender, "Propriétaires : %1$s", str.toString());
		}

		final Collection<UUID> members = region.getMembers(false);
		if (!members.isEmpty())
		{
			final StringBuilder str = new StringBuilder();

			for (final Iterator<UUID> it = members.iterator(); it.hasNext();)
			{
				str.append(plugin.getUniqueIdProvider().getNameFromUniqueId(it.next()));

				if (it.hasNext())
					str.append(", ");
			}

			ChatUtil.sendMessage(sender, "Membres : %1$s", str.toString());
		}
	}
}
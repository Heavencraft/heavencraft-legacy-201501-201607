package fr.heavencraft.heavenguard.bukkit.commands.owner;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class InfoSubCommand extends AbstractOwnerSubCommand
{
	public InfoSubCommand(RegionProvider regionProvider)
	{
		super(regionProvider, HeavenGuardPermissions.INFO_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		info(sender, regionName);
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		HeavenGuard.sendMessage(sender, "/rg {info} <protection>");
	}

	private void info(CommandSender sender, String name) throws HeavenException, UserNotFoundException
	{
		final Region region = regionProvider.getRegionByName(name);

		HeavenGuard.sendMessage(sender, "Protection : %1$s", region.getName());
		HeavenGuard.sendMessage(sender, "Coordonnées : [{%1$s %2$s %3$s}] -> [{%4$s %5$s %6$s}] ({%7$s})", //
				region.getMinX(), region.getMinY(), region.getMinZ(), //
				region.getMaxX(), region.getMaxY(), region.getMaxZ(), //
				region.getWorld());

		/*
		 * Flags
		 */

		String flags = "Flags : ";

		for (final Entry<Flag, Boolean> flag : region.getBooleanFlags().entrySet())
		{
			if (flag.getValue() != null)
				flags += flag.getKey().getName() + " : " + flag.getValue() + ", ";
		}

		HeavenGuard.sendMessage(sender, flags);

		final Region parent = region.getParent();
		if (parent != null)
			HeavenGuard.sendMessage(sender, "Parent : %1$s", parent.getName());

		final Collection<UUID> owners = region.getMembers(true);
		if (!owners.isEmpty())
		{
			final StringBuilder str = new StringBuilder();

			for (final Iterator<UUID> it = owners.iterator(); it.hasNext();)
			{
				str.append(HeavenGuard.getInstance().getUniqueIdProvider().getNameFromUniqueId(it.next()));

				if (it.hasNext())
					str.append(", ");
			}

			HeavenGuard.sendMessage(sender, "Propriétaires : %1$s", str.toString());
		}

		final Collection<UUID> members = region.getMembers(false);
		if (!members.isEmpty())
		{
			final StringBuilder str = new StringBuilder();

			for (final Iterator<UUID> it = members.iterator(); it.hasNext();)
			{
				str.append(HeavenGuard.getInstance().getUniqueIdProvider().getNameFromUniqueId(it.next()));

				if (it.hasNext())
					str.append(", ");
			}

			HeavenGuard.sendMessage(sender, "Membres : %1$s", str.toString());
		}
	}
}
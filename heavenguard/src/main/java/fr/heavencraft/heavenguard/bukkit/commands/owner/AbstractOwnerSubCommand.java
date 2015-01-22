package fr.heavencraft.heavenguard.bukkit.commands.owner;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.commands.AbstractSubCommand;

abstract class AbstractOwnerSubCommand extends AbstractSubCommand
{
	protected AbstractOwnerSubCommand(RegionProvider regionProvider, String permission)
	{
		super(regionProvider, permission);
	}

	@Override
	public boolean canExecute(CommandSender sender, String regionName)
	{
		if (super.canExecute(sender, regionName))
			return true;

		if (sender instanceof Player)
		{
			try
			{
				return regionProvider.getRegionByName(regionName).isMember(((Player) sender).getUniqueId(), true);
			}
			catch (final HeavenException ex)
			{
				return false;
			}
		}

		return false;
	}
}
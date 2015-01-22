package fr.heavencraft.heavenguard.bukkit.commands;

import java.sql.Types;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class FlagSubCommand extends AbstractSubCommand
{
	public FlagSubCommand(RegionProvider regionProvider)
	{
		super(regionProvider, HeavenGuardPermissions.FLAG_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		switch (args.length)
		{
			case 1: // Remove the flag from the region.
				flag(sender, regionName, args[0], null);
				break;

			case 2: // Set the flag's value.
				flag(sender, regionName, args[0], args[1]);
				break;

			default:
				sendUsage(sender);
				break;
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		HeavenGuard.sendMessage(sender, "/rg {flag} <protection> <flag> : pour supprimer un flag");
		HeavenGuard.sendMessage(sender, "/rg {flag} <protection> <flag> <valeur> : pour ajouter un flag");
	}

	private void flag(CommandSender sender, String regionName, String flagName, String value) throws HeavenException
	{
		final Flag flag = Flag.getUniqueInstanceByName(flagName);

		if (flag == null)
			throw new HeavenException("Le flag {%1$s} n'existe pas.", flagName);

		final Region region = regionProvider.getRegionByName(regionName);

		switch (flag.getType())
		{
			case Types.BIT:
				region.setBooleanFlag(flag, value != null ? Boolean.parseBoolean(value) : null);
				break;
		}

		HeavenGuard.sendMessage(sender, "La protection {%1$s} a désormais : {%2$s} = {%3$s}", region.getName(), flag.getName(),
				value);
	}
}
package fr.heavencraft.heavenguard.bukkit.commands;

import java.sql.Timestamp;
import java.util.Date;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DateUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class FlagSubCommand extends AbstractSubCommand
{
	public FlagSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.FLAG_COMMAND);
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
		ChatUtil.sendMessage(sender, "/rg {flag} <protection> <flag> : pour supprimer un flag");
		ChatUtil.sendMessage(sender, "/rg {flag} <protection> <flag> <valeur> : pour ajouter un flag");
	}

	private void flag(CommandSender sender, String regionName, String flagName, String value)
			throws HeavenException
	{
		final Flag flag = Flag.getUniqueInstanceByName(flagName);

		if (flag == null)
			throw new HeavenException("Le flag {%1$s} n'existe pas.", flagName);

		final Region region = plugin.getRegionProvider().getRegionByName(regionName);

		switch (flag.getType())
		{
			case BOOLEAN:
				region.getFlagHandler().setBooleanFlag(flag, value != null ? Boolean.parseBoolean(value) : null);
				break;

			case TIMESTAMP:
				if (value == null)
					region.getFlagHandler().setTimestampFlag(flag, null);

				else
				{
					final Date date = DateUtil.parseDateTime(value);
					region.getFlagHandler().setTimestampFlag(flag, new Timestamp(date.getTime()));
				}
				break;

			case BYTE_ARRAY:
				throw new HeavenException("Ce type de flag ne peut être changé via /rg flag.");
		}

		ChatUtil.sendMessage(sender, "La protection {%1$s} a désormais : {%2$s} = {%3$s}", region.getName(),
				flag.getName(), value);
	}
}
package fr.heavencraft.heavenguard.bukkit.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;
import fr.heavencraft.heavenguard.bukkit.WorldEditUtil;

public class LoadStateSubCommand extends AbstractSubCommand
{

	public LoadStateSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.INFO_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Region region = plugin.getRegionProvider().getRegionByName(regionName);
		final byte[] schematic = region.getFlagHandler().getByteArrayFlag(Flag.STATE);

		if (schematic == null)
			throw new HeavenException("Cette protection n'a pas de schematic enregistré.");

		final World world = Bukkit.getWorld(region.getWorld());
		final Location pos1 = new Location(world, region.getMinX(), region.getMinY(), region.getMinZ());
		final Location pos2 = new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ());

		try
		{
			WorldEditUtil.load(schematic, world, pos1, pos2);
			plugin.sendMessage(sender, "Done");
		}
		catch (final IOException e)
		{
			e.printStackTrace();
			plugin.sendMessage(sender, "Fail");
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
	}
}
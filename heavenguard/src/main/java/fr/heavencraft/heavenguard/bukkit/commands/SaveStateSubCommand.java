package fr.heavencraft.heavenguard.bukkit.commands;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.sk89q.worldedit.WorldEditException;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.Flag;
import fr.heavencraft.heavenguard.api.HeavenGuardPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;
import fr.heavencraft.heavenguard.bukkit.WorldEditUtil;

public class SaveStateSubCommand extends AbstractSubCommand
{
	public SaveStateSubCommand(HeavenGuard plugin)
	{
		super(plugin, HeavenGuardPermissions.INFO_COMMAND);
	}

	@Override
	public void execute(CommandSender sender, String regionName, String[] args) throws HeavenException
	{
		final Region region = plugin.getRegionProvider().getRegionByName(regionName);
		final World world = Bukkit.getWorld(region.getWorld());
		final Location pos1 = new Location(world, region.getMinX(), region.getMinY(), region.getMinZ());
		final Location pos2 = new Location(world, region.getMaxX(), region.getMaxY(), region.getMaxZ());

		try
		{
			final byte[] schematic = WorldEditUtil.save(world, pos1, pos2);

			region.getFlagHandler().setByteArrayFlag(Flag.STATE, schematic);
			plugin.sendMessage(sender, "Done");
		}
		catch (WorldEditException | IOException e)
		{
			e.printStackTrace();
			plugin.sendMessage(sender, "Fail");
		}
	}

	@Override
	public void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}

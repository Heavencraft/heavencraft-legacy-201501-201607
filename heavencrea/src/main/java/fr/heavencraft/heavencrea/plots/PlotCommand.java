package fr.heavencraft.heavencrea.plots;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlotCommand extends AbstractCommandExecutor
{
	private final HeavenGuard hGuard;

	public PlotCommand(HeavenPlugin plugin, HeavenGuard hGuard)
	{
		super(plugin, "plot", "heavencrea.plot", null);

		this.hGuard = hGuard;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(sender);
			return;
		}

		switch (args[0])
		{
			case "clear":
				final Region region = hGuard.getRegionProvider().getRegionByName(args[1].toLowerCase());

				final World world = Bukkit.getWorld(region.getWorld());
				final int minX = region.getMinX();
				final int maxX = region.getMaxX();
				final int minZ = region.getMinZ();
				final int maxZ = region.getMaxZ();

				clearPlot(world, minX, maxX, minZ, maxZ, Material.REDSTONE_BLOCK);
				break;

			default:
				sendUsage(sender);
				break;
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/{plot} clear <protection>");
	}

	private void clearPlot(final World world, final int minX, final int maxX, final int minZ, final int maxZ,
			final Material border) throws HeavenException
	{
		for (int x = minX; x <= maxX; x++)
		{
			for (int z = minZ; z <= maxZ; z++)
			{
				// O -> 4 : BEDROCK
				for (int y = 0; y != 5; y++)
					world.getBlockAt(x, y, z).setType(Material.BEDROCK, false);

				// 5 -> 45 : STONE
				for (int y = 5; y != 46; y++)
					world.getBlockAt(x, y, z).setType(Material.STONE, false);

				// 46 -> 49 : DIRT
				for (int y = 46; y != 50; y++)
					world.getBlockAt(x, y, z).setType(Material.DIRT, false);

				// 50 : GRASS
				world.getBlockAt(x, 50, z).setType(Material.GRASS, false);

				if (x == minX || x == maxX || z == minZ || z == maxZ)
					world.getBlockAt(x, 51, z).setType(border, false);
				else
					world.getBlockAt(x, 51, z).setType(Material.AIR, false);

				// 52 -> 255 : AIR
				for (int y = 52; y != 256; y++)
					world.getBlockAt(x, y, z).setType(Material.AIR, false);
			}
		}
	}
}
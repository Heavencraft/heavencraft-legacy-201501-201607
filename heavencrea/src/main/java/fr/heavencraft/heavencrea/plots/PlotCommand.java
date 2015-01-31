package fr.heavencraft.heavencrea.plots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencrea.CreaPermissions;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.HeavenGuard;

public class PlotCommand extends AbstractCommandExecutor
{
	private final HeavenGuard hGuard;

	public PlotCommand(HeavenPlugin plugin, HeavenGuard hGuard)
	{
		super(plugin, "plot", CreaPermissions.PLOT_COMMAND);

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
		if (args.length != 2)
		{
			sendUsage(sender);
			return;
		}

		final Region region = hGuard.getRegionProvider().getRegionByName(args[1].toLowerCase());

		switch (args[0])
		{
			case "tp":
				if (sender instanceof Player)
				{
					final World world = Bukkit.getWorld(region.getWorld());
					final int x = region.getMinX();
					final int z = region.getMinZ();

					((Player) sender).teleport(new Location(world, x, 51, z));
					plugin.sendMessage(sender, "Vous avez été téléporté à la parcelle {%1$s}.", region.getName());
				}
				break;

			case "clear":
				clearPlot(region, Material.REDSTONE_BLOCK);
				plugin.sendMessage(sender, "La parcelle {%1$s} a bien été nettoyée.", region.getName());
				break;

			case "remove":
				clearPlot(region, Material.EMERALD_BLOCK);
				hGuard.getRegionProvider().deleteRegion(region.getName());
				plugin.sendMessage(sender, "La parcelle {%1$s} a bien été supprimée.", region.getName());
				break;

			default:
				sendUsage(sender);
				break;
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		plugin.sendMessage(sender, "/{plot} tp <protection>");
		plugin.sendMessage(sender, "/{plot} clear <protection>");
		plugin.sendMessage(sender, "/{plot} remove <protection>");
	}

	private void clearPlot(Region region, final Material border) throws HeavenException
	{
		final World world = Bukkit.getWorld(region.getWorld());
		final int minX = region.getMinX();
		final int maxX = region.getMaxX();
		final int minZ = region.getMinZ();
		final int maxZ = region.getMaxZ();

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
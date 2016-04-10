package fr.heavencraft.heavencrea.plots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenguard.api.Region;
import fr.heavencraft.heavenguard.bukkit.BukkitHeavenGuard;

public final class ParcelleCommand extends AbstractCommandExecutor
{
	private final BukkitHeavenGuard hGuard;

	public ParcelleCommand(HeavenPlugin plugin, BukkitHeavenGuard hGuard)
	{
		super(plugin, "parcelle");

		this.hGuard = hGuard;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length != 2)
		{
			sendUsage(player);
			return;
		}

		switch (args[0])
		{
			case "tp":
				final Region region = hGuard.getRegionProvider().getRegionByName(args[1].toLowerCase());

				if (!region.isMember(player.getUniqueId(), false))
					throw new HeavenException("Vous n'êtes pas membre de la parcelle {%1$s}.", region.getName());

				final World world = Bukkit.getWorld(region.getWorld());
				final int x = region.getMinX();
				final int z = region.getMinZ();

				PlayerUtil.teleportPlayer(player, new Location(world, x, 52, z),
						"Vous avez été téléporté à la parcelle {%1$s}.", region.getName());
				break;

			default:
				sendUsage(player);
				break;
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{parcelle} tp <protection> : se téléporter votre parcelle");
	}
}
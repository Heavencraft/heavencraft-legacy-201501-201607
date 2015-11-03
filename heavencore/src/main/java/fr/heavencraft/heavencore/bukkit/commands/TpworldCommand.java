package fr.heavencraft.heavencore.bukkit.commands;

import static fr.heavencraft.heavencore.utils.chat.ChatUtil.sendMessage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.CorePermissions;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class TpworldCommand extends AbstractCommandExecutor
{
	public TpworldCommand(HeavenPlugin plugin)
	{
		super(plugin, "tpworld", CorePermissions.TPWORLD_COMMAND);
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		if (args.length != 1)
		{
			sendUsage(player);
			return;
		}

		final World world = Bukkit.getWorld(args[0]);

		if (world == null)
			throw new HeavenException("Le monde {%1$s} n'existe pas.", args[0]);

		Location location = player.getLocation();
		location.setWorld(world);

		PlayerUtil.teleportPlayer(player, location, "Vous avez été téléporté dans le monde {%1$s}.",
				world.getName());
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		sendMessage(sender, "/{tpworld} <monde>");
	}
}
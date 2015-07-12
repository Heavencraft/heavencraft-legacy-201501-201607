package fr.heavencraft.hellcraft.hps;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.hellcraft.HellCraftPermissions;

public class PtimeCommand extends AbstractCommandExecutor
{
	private static final long DAY = 1000;
	private static final long NIGHT = 13000;

	public PtimeCommand(HeavenPlugin plugin)
	{
		super(plugin, "ptime", HellCraftPermissions.PTIME_COMMAND);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		switch (args[0].toLowerCase())
		{
			case "day":
				player.setPlayerTime(DAY, false);
				break;

			case "night":
				player.setPlayerTime(NIGHT, false);
				break;

			case "reset":
				player.resetPlayerTime();
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
		plugin.sendMessage(sender, "/{ptime} <day|night> : permet de mettre le jour ou la nuit.");
		plugin.sendMessage(sender, "/{ptime} reset : permet de remettre l'heure du serveur.");
	}
}
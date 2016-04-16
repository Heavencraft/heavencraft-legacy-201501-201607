package fr.heavencraft.heavenevent.timer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;

public class TimerCommands extends AbstractCommandExecutor
{

	private final String ERRORPERMISSION = "Vous ne pouvez pas faire ça.";
	private final String STARTERROR = "Il y a déjà une partie en cours.";
	private final String STOPERROR = "Il n'y a pas de partie en cours";
	private final String SUCCESS = "Timer sauvegardé";

	public TimerCommands(HeavenPlugin plugin)
	{
		super(plugin, "timer");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (!player.isOp())
			throw new HeavenException(ERRORPERMISSION);
		if (args.length <= 0)
		{
			sendUsage(player);
			return;
		}

		// Launch Timer
		if (args[0].equalsIgnoreCase("start"))
		{
			// if Timer already running
			if (TimerConfigurationEditor.start)
				throw new HeavenException(STARTERROR);

			// Save Started Time of the event
			player.sendMessage(SUCCESS);

			// launch Scoreboard
			TimerScoreboard.initScoreboard();
		}

		// Stop Timer
		else if (args[0].equalsIgnoreCase("stop"))
		{
			// if there is no Timer running
			if (!TimerConfigurationEditor.start)
				throw new HeavenException(STOPERROR);

			// Reset Event Timer
			TimerScoreboard.stopScoreboard();
			TimerConfigurationEditor.resetConfig();
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args[0].equalsIgnoreCase("stop"))
		{
			// if there is no Timer running
			if (!TimerConfigurationEditor.start)
				throw new HeavenException(STOPERROR);

			// Reset Event Timer
			TimerScoreboard.stopScoreboard();
			TimerConfigurationEditor.resetConfig();
		}

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{timer} start");
		ChatUtil.sendMessage(sender, "/{timer} stop");
	}

}

package fr.heavencraft.heavenevent.timer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class TimerCommands extends AbstractCommandExecutor
{

	private final String ERRORPERMISSION = "Vous ne pouvez pas faire ça.";
	private final String STARTERROR = "Il y a déjà une partie en cours.";
	private final String STOPERROR = "Il n'y a pas de partie en cours";

	public TimerCommands(HeavenPlugin plugin)
	{
		super(plugin, "timer");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (!player.isOp())
			throw new HeavenException(ERRORPERMISSION);
		if (args[0].equalsIgnoreCase("start"))
		{
			if (TimerConfigurationEditor.start)
				throw new HeavenException(STARTERROR);

			final String RESULT = TimerConfigurationEditor.saveCurrentTime();
			player.sendMessage(RESULT);

			TimerScoreboard.initScoreboard();
		}
		else if (args[0].equalsIgnoreCase("stop"))
		{
			if (!TimerConfigurationEditor.start)
				throw new HeavenException(STOPERROR);

			TimerScoreboard.stopScoreboard();
			TimerConfigurationEditor.resetConfig();
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}

}

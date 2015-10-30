package fr.heavencraft.heavenrp.commands.key;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.key.KeyManager;
import fr.heavencraft.utils.PlayerUtil;

public class KeyCommand extends HeavenCommand
{

	public KeyCommand()
	{
		super("key", RPPermissions.KEY);
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
			return;

		Player player = PlayerUtil.getPlayer(args[0]);
		String key = args[1];

		KeyManager.giveKey(player, key);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
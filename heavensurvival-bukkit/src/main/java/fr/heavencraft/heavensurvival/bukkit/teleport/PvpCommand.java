package fr.heavencraft.heavensurvival.bukkit.teleport;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavensurvival.common.users.UpdateUserPvpQuery;
import fr.heavencraft.heavensurvival.common.users.User;
import fr.heavencraft.heavensurvival.common.users.UserProvider;

public class PvpCommand extends AbstractCommandExecutor
{
	public PvpCommand(HeavenPlugin plugin)
	{
		super(plugin, "pvp");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final User user = UserProvider.getInstance().getUserByUniqueId(player.getUniqueId());

		if (user.isPvp())
		{
			QueriesHandler.addQuery(new UpdateUserPvpQuery(user, false));
			ChatUtil.sendMessage(player, "Le pvp a été désactivé");
		}

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
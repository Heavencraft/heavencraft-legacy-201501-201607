package fr.heavencraft.heavenfun.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.users.balance.UpdateUserBalanceQuery;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenfun.common.FunPermissions;
import fr.heavencraft.heavenfun.common.users.FunUser;
import fr.heavencraft.heavenfun.common.users.FunUserProvider;

public class FunPointsAdminCommand extends AbstractCommandExecutor
{

	public FunPointsAdminCommand(HeavenPlugin plugin)
	{
		super(plugin, "fpa", FunPermissions.FPA);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length == 3 && args[0].equals("give"))
		{
			onGiveCommand(sender, PlayerUtil.getExactName(args[1]), DevUtil.toUint(args[2]));
		}
		else
		{
			sendUsage(sender);
		}
	}

	private void onGiveCommand(CommandSender sender, String playerName, int amount) throws HeavenException
	{
		final FunUser user = FunUserProvider.get().getUserByName(playerName);

		QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, amount, FunUserProvider.get())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(sender, "{%1$s} a reçu {%2$s %3$s}.", user.getName(), amount,
						user.getCurrencyName());
			}
		});
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{fpa} give <joueur> <montant>");
	}

}

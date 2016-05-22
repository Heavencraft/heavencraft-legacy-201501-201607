package fr.heavencraft.heavenfun.economy;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.exceptions.PlayerNotConnectedException;
import fr.heavencraft.heavencore.users.balance.UpdateUserBalanceQuery;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenfun.common.HeavenFun;
import fr.heavencraft.heavenfun.common.users.FunUser;
import fr.heavencraft.heavenfun.common.users.FunUserProvider;

public class FunPointsCommand extends AbstractCommandExecutor
{
	private final static String BALANCE_SELF = "Vous possedez {%1$s}.";
	private final static String BALANCE_OTHER = "{%1$s} possede {%2$s}.";
	private final static String MONEY_GIVE = "Vous avez envoyé {%1$s %2$s} à {%3$s}.";
	private final static String MONEY_RECEIVE = "Vous avez reçu {%1$s %2$s} de {%3$s}.";

	public FunPointsCommand(HeavenPlugin plugin)
	{
		super(plugin, "fp");
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

		final String action = args[0];

		// /fp voir
		if (action.equals("voir") && args.length == 1)
		{
			final FunUser user = FunUserProvider.get().getUserByName(sender.getName());
			ChatUtil.sendMessage(sender, BALANCE_SELF, user.getBalanceString());
		}
		// /fp voir <joueur>
		else if (action.equals("voir") && args.length == 2)
		{
			final FunUser user = FunUserProvider.get().getUserByName(PlayerUtil.getExactName(args[1]));
			ChatUtil.sendMessage(sender, BALANCE_OTHER, user.getName(), user.getBalanceString());
		}
		// /fp donner <joueur> <nombre>
		else if (action.equals("donner") && args.length == 3)
		{
			final FunUser user = FunUserProvider.get().getUserByName(PlayerUtil.getExactName(args[1]));
			final int delta = DevUtil.toUint(args[2]);

			QueriesHandler.addQuery(new UpdateUserBalanceQuery(user, delta, FunUserProvider.get())
			{
				@Override
				public void onSuccess()
				{
					final String username = user.getName();
					ChatUtil.sendMessage(sender, MONEY_GIVE, delta, HeavenFun.CURRENCY, username);

					try
					{
						ChatUtil.sendMessage(PlayerUtil.getPlayer(username), MONEY_RECEIVE, delta, HeavenFun.CURRENCY,
								sender.getName());
					}
					catch (final PlayerNotConnectedException e)
					{
					}
				}
			});
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{fp}");
		ChatUtil.sendMessage(sender, "/{fp} voir");
		ChatUtil.sendMessage(sender, "/{fp} voir <joueur>");
		ChatUtil.sendMessage(sender, "/{fp} donner <joueur> <nombre>");
	}
}
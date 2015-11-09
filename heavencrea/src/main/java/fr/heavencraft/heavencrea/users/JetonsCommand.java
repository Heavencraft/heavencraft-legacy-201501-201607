package fr.heavencraft.heavencrea.users;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.users.UserProvider;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class JetonsCommand extends AbstractCommandExecutor
{
	private final UserProvider<CreativeUser> userProvider;

	public JetonsCommand(HeavenCrea plugin)
	{
		super(plugin, "jetons", Arrays.asList("jeton", "money"));
		userProvider = plugin.getUserProvider();
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final UUID uuid = player.getUniqueId();
		final CreativeUser user = userProvider.getUserByUniqueId(uuid);

		if (args.length == 0)
		{
			ChatUtil.sendMessage(player, "Vous avez {%1$s} jetons sur vous.", user.getBalance());
			return;
		}

		switch (args[0])
		{
			case "donner":
				if (args.length == 3)
				{
					final OfflinePlayer dest = PlayerUtil.getOfflinePlayer(args[1]);
					final int delta = DevUtil.toUint(args[2]);

					final CreativeUser receiver = userProvider.getUserByUniqueId(dest.getUniqueId());

					user.updateBalance(-delta);
					receiver.updateBalance(delta);

					ChatUtil.sendMessage(player, "Vous avez donné {%1$s} jetons à {%2$s}.", delta,
							dest.getName());
				}
				else
					sendUsage(player);
				break;

			default:
				sendUsage(player);
				break;
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length != 3)
			return;

		userProvider.getUserByName(args[1]).updateBalance(Integer.parseInt(args[2]));
		Bukkit.getLogger().log(Level.INFO, "Solde mis a jour");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{jetons}");
		ChatUtil.sendMessage(sender, "/{jetons} donner <player> <nombre de jetons>");
	}
}
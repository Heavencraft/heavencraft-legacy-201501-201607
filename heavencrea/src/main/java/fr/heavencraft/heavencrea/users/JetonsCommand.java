package fr.heavencraft.heavencrea.users;

import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.PlayerUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class JetonsCommand extends AbstractCommandExecutor
{
	private final UserProvider userProvider;

	public JetonsCommand(HeavenCrea plugin)
	{
		super(plugin, "jetons", Arrays.asList("jeton"));
		userProvider = plugin.getUserProvider();
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		final UUID uuid = player.getUniqueId();
		final User user = userProvider.getUserByUniqueId(uuid);

		if (args.length == 0)
		{
			plugin.sendMessage(player, "Vous avez {%1$s} jetons sur vous.", user.getBalance());
			return;
		}

		switch (args[0])
		{
			case "donner":
				if (args.length == 3)
				{
					final String dest = PlayerUtil.getExactName(args[1]);
					final int delta = DevUtil.toUint(args[2]);

					final User receiver = userProvider.getUserByName(dest);

					user.updateBalance(-delta);
					receiver.updateBalance(delta);

					plugin.sendMessage(player, "Vous avez donné {%1$s} jetons à {%2$s}.", delta, dest);
				}
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
		plugin.sendMessage(sender, "/{jetons} give <player> <Jeton> : {Pas disponnible");
	}
}
package fr.heavencraft.heavenrp.jobs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavenrp.database.users.SetJobQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class JobsCommand extends AbstractCommandExecutor
{

	public JobsCommand(HeavenPlugin plugin)
	{
		super(plugin, "jobs");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		displayJob(player);
	}

	private void displayJob(Player player) throws HeavenException
	{
		final User user = UserProvider.getUserByName(player.getName());

		final Job job = user.getJob();
		if (job == null)
			throw new HeavenException("Vous n'avez actuellement pas de métier.");

		final int currentXp = user.getJobExperience();
		final int currentLevel = JobUtil.getLevelFromXp(currentXp);

		final long minXpLevel = JobUtil.getXpToLevel(currentLevel);
		final long maxXpLevel = JobUtil.getXpToLevel(currentLevel + 1);

		final String barLine = ChatUtil.createBar(currentXp - minXpLevel, maxXpLevel - minXpLevel);

		ChatUtil.sendMessage(player, "Vous êtes actuellement un %1$s %2$s niveau %3$s.", job.getName(),
				Rank.getRankByLevel(currentLevel), currentLevel);
		ChatUtil.sendMessage(player, "Prochain niveau : %1$s", barLine);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		switch (args[1])
		{
			case "set":
				if (args.length == 3)
				{
					final User user = UserProvider.getUserByName(args[0]);
					final Job job = JobsProvider.getJobById(Integer.parseInt(args[2]));

					QueriesHandler.addQuery(new SetJobQuery(user, job)
					{
						@Override
						public void onSuccess()
						{
							ChatUtil.sendMessage(sender, "Métier du joueur changé");
						}
					});
				}
				break;
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{jobs} pour savoir son métier");
		ChatUtil.sendMessage(sender, "/{jobs} <joueur> set <metier> pour changer le métier d'un joueur");
	}
}
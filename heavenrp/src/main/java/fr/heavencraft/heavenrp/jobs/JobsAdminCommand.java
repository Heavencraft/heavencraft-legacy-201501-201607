package fr.heavencraft.heavenrp.jobs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.async.queries.QueriesHandler;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.heavenrp.database.users.SetJobQuery;
import fr.heavencraft.heavenrp.database.users.UpdateJobExperienceQuery;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;

public class JobsAdminCommand extends AbstractCommandExecutor
{
	public JobsAdminCommand(HeavenPlugin plugin)
	{
		super(plugin, "jobsadmin", RPPermissions.JOBSADMIN_COMMAND);
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

		switch (args[0].toLowerCase())
		{
			case "info":
			{
				if (args.length != 2)
					break;

				final User user = UserProvider.getUserByName(PlayerUtil.getExactName(args[1]));
				final int xp = user.getJobExperience();
				final int level = JobUtil.getLevelFromXp(xp);

				ChatUtil.sendMessage(sender, "Métier: ", user.getJob());
				ChatUtil.sendMessage(sender, "Expérience: %1$s", xp);
				ChatUtil.sendMessage(sender, "Niveau: %1$s", level);
				ChatUtil.sendMessage(sender, "Prochain niveau: %1$s xp", JobUtil.getXpToLevel(level + 1) - xp);
				return;
			}
			case "setjob":
			{
				if (args.length != 3)
					break;

				final User user = UserProvider.getUserByName(PlayerUtil.getExactName(args[1]));
				final Job job = JobsProvider.getJobById(Integer.parseInt(args[2]));

				QueriesHandler.addQuery(new SetJobQuery(user, job)
				{
					@Override
					public void onSuccess()
					{
						ChatUtil.sendMessage(sender, "Le joueur %1$s est désormais %2$s.", user.getName(),
								job.getName());
					}
				});
				return;
			}
			case "updatexp":
			{
				if (args.length != 3)
					break;

				final User user = UserProvider.getUserByName(PlayerUtil.getExactName(args[1]));
				final int delta = DevUtil.toInt(args[2]);

				QueriesHandler.addQuery(new UpdateJobExperienceQuery(user, delta)
				{
					@Override
					public void onSuccess()
					{
						ChatUtil.sendMessage(sender, "Le joueur %1$s a été mis à jour.", user.getName());
					}
				});
			}
		}
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "/{jobsadmin} info <joueur> pour savoir son métier");
		ChatUtil.sendMessage(sender, "/{jobsadmin} setjob <joueur> <metier> pour changer le métier d'un joueur");
		ChatUtil.sendMessage(sender, "/{jobsadmin} updatexp <joueur> <delta> pour changer l'xp d'un joueur");
	}
}
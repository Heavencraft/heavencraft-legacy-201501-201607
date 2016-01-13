package fr.heavencraft.heavenrp.jobs;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
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
		final User user = UserProvider.getUserByName(player.getName());

		final Job job = user.getJob();
		if (job == null)
		{
			ChatUtil.sendMessage(player, "Vous n'avez actuellement pas de métier.");
		}
		else
		{
			ChatUtil.sendMessage(player, "Vous êtes actuellement un %1$s %2$s.", job.getDisplayName(),
					Rank.getRankByLevel(JobUtil.getLevelFromXp(user.getJobExperience())));
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
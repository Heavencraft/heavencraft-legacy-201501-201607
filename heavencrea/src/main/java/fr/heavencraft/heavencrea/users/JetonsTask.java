package fr.heavencraft.heavencrea.users;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencrea.HeavenCrea;

public class JetonsTask extends BukkitRunnable
{
	private static final long PERIOD = 18000; // 15 minutes : 20 * 60 * 15 ticks
	private final HeavenCrea plugin;

	public JetonsTask(HeavenCrea plugin)
	{
		runTaskTimer(plugin, PERIOD, PERIOD);

		this.plugin = plugin;
	}

	@Override
	public void run()
	{
		final Collection<? extends Player> players = Bukkit.getOnlinePlayers();
		final int nbJetons = players.size() > 5 ? players.size() : 5;

		for (final Player player : players)
		{
			try
			{
				plugin.getUserProvider().getUserByUniqueId(player.getUniqueId()).updateBalance(nbJetons);
				ChatUtil.sendMessage(player, "Vous venez de recevoir {%1$s} jetons.", nbJetons);
			}
			catch (final HeavenException ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
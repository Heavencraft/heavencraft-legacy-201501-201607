package fr.heavencraft.deprecated;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.users.color.ScoreboardTeam;
import fr.heavencraft.heavencore.users.color.TabColor;

public class DeprecatedTabColorScoreboard<P extends HeavenPlugin & DeprecatedHasUserProvider<U>, U extends DeprecatedUser>
{
	private final Map<TabColor, ScoreboardTeam> teams = new HashMap<TabColor, ScoreboardTeam>();

	public DeprecatedTabColorScoreboard(P plugin)
	{
		final Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

		// Unregister teams previously registered
		for (final Team team : scoreboard.getTeams())
		{
			team.unregister();
		}

		// Register one team per color except white
		for (final TabColor color : TabColor.getAllColors())
		{
			if (color == TabColor.WHITE)
				continue;

			System.out.println("Register color " + color.getName());
			final ScoreboardTeam team = new ScoreboardTeam(color.getName(), color);
			team.register(scoreboard, plugin);

			teams.put(color, team);
		}

		for (final DeprecatedUser user : plugin.getUserProvider().getAllUsers())
		{
			setPlayerColor(Bukkit.getOfflinePlayer(user.getUniqueId()), user.getTabColor());
		}
	}

	public void setPlayerColor(OfflinePlayer player, TabColor color)
	{
		final ScoreboardTeam team = teams.get(color);

		if (team == null)
		{
			for (final ScoreboardTeam team2 : teams.values())
				team2.removePlayer(player);

			return;
		}

		team.addPlayer(player);
	}
}
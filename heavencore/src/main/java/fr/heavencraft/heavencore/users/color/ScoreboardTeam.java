package fr.heavencraft.heavencore.users.color;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;

public class ScoreboardTeam
{
	private static final String COLOR_COMMAND_FORMAT = "scoreboard teams option %1$s color %2$s";

	private final String name;
	private final String colorCommand;

	private Team team;

	public ScoreboardTeam(String name, TabColor color)
	{
		this.name = name;
		this.colorCommand = String.format(COLOR_COMMAND_FORMAT, name, color.getName());
	}

	public void register(Scoreboard scoreboard, HeavenPlugin plugin)
	{
		team = scoreboard.registerNewTeam(name);
		team.setNameTagVisibility(NameTagVisibility.ALWAYS);
		Bukkit.getScheduler().runTask(plugin, new Runnable()
		{

			@Override
			public void run()
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), colorCommand);
			}
		});
	}

	public void addPlayer(OfflinePlayer player)
	{
		if (!team.hasPlayer(player))
			team.addPlayer(player);
	}

	public void removePlayer(OfflinePlayer player)
	{
		if (team.hasPlayer(player))
			team.removePlayer(player);
	}
}
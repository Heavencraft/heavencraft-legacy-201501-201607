package fr.heavencraft.heavencore.users.color;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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

	public void register(Scoreboard scoreboard, JavaPlugin plugin)
	{
		team = scoreboard.registerNewTeam(name);
		// TODO : change this after 1.9 update
		team.setNameTagVisibility(NameTagVisibility.ALWAYS);
		// team.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.ALWAYS);
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
		if (!team.hasEntry(player.getName()))
			team.addEntry(player.getName());
	}

	public void removePlayer(OfflinePlayer player)
	{
		if (team.hasEntry(player.getName()))
			team.removeEntry(player.getName());
	}
}
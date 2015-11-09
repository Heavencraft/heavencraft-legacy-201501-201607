package fr.heavencraft.heavenrp.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.heavencraft.heavenrp.provinces.ProvincesManager.Province;

public class ProvinceScoreboard
{
	public static void initialize()
	{
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

		// Unregister teams previously registered
		for (Team team : scoreboard.getTeams())
		{
			team.unregister();
		}

		// Register teams from ProvinceTeam
		for (ProvinceTeam team : ProvinceTeam.getAllTeams())
		{
			team.register(scoreboard);
		}
	}

	public static void applyTeamColor(OfflinePlayer player, Province province)
	{
		// Remove player from all teams
		for (ProvinceTeam team : ProvinceTeam.getAllTeams())
			team.removePlayer(player);

		// If null, do nothing. --> player does not have any province
		if (province == null)
			return;

		ProvinceTeam.getTeamById(province.getId()).addPlayer(player);
	}
}
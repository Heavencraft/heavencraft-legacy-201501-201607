package fr.heavencraft.heavencore.utils;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.exceptions.PlayerNotConnectedException;

public class PlayerUtil
{
	public static Player getPlayer(String playerName) throws PlayerNotConnectedException
	{
		@SuppressWarnings("deprecation")
		final Player player = Bukkit.getPlayer(playerName);

		if (player == null)
			throw new PlayerNotConnectedException(playerName);

		return player;
	}

	public static String getExactName(String playerName)
	{
		try
		{
			return getPlayer(playerName).getName();
		}
		catch (final PlayerNotConnectedException ex)
		{
			return playerName;
		}
	}

	@SuppressWarnings("deprecation")
	public static OfflinePlayer getOfflinePlayer(String playerName)
	{
		try
		{
			return getPlayer(playerName);
		}
		catch (final PlayerNotConnectedException ex)
		{
			return Bukkit.getOfflinePlayer(playerName);
		}
	}
}
package fr.heavencraft.Utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.heavencraft.exceptions.PlayerNotConnectedException;

public class ChatUtil
{
	private static final String BEGIN = "{";
	private static final String END = "}";
	private static final String ERROR_COLOR = ChatColor.RED.toString();
	private static final String NORMAL_COLOR = ChatColor.GOLD.toString();
	private static final String BROADCAST_COLOR = ChatColor.AQUA.toString();
	private static final String BROADCAST_COLOR_H = ChatColor.GREEN.toString();

	/*
	 * Send message to a player
	 */

	public static void sendMessage(CommandSender sender, String message)
	{
		if (sender != null)
			sender.sendMessage(NORMAL_COLOR + message.replace(BEGIN, ERROR_COLOR).replace(END, NORMAL_COLOR));
	}

	public static void sendMessage(CommandSender sender, String message, Object... args)
	{
		sendMessage(sender, String.format(message, args));
	}

	public static void sendMessage(List<CommandSender> senders, String message, Object... args)
	{
		for (CommandSender sender : senders)
			sendMessage(sender, message, args);
	}

	public static void sendMessage(String playerName, String message) throws PlayerNotConnectedException
	{
		sendMessage(Bukkit.getPlayer(playerName), message);
	}

	public static void sendMessage(String playerName, String message, Object... args) throws PlayerNotConnectedException
	{
		sendMessage(Bukkit.getPlayer(playerName), message, args);
	}

	/*
	 * Broadcast message to all players
	 */
	public static void broadcastMessage(String message)
	{
		Bukkit.broadcastMessage(BROADCAST_COLOR
				+ message.replace(BEGIN, BROADCAST_COLOR_H).replace(END, BROADCAST_COLOR));
	}

	public static void broadcastMessage(String message, Object... args)
	{
		broadcastMessage(String.format(message, args));
	}
}

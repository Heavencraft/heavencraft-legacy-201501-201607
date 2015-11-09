package fr.heavencraft.heavencore.utils.chat;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.heavencore.exceptions.PlayerNotConnectedException;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public class ChatUtil
{
	static String errorColor = ChatColor.RED.toString();
	static String normalColor = ChatColor.GOLD.toString();

	public void setNormalColor(ChatColor color)
	{
		normalColor = color.toString();
	}

	public void setErrorColor(ChatColor color)
	{
		errorColor = color.toString();
	}

	/*
	 * Send message to a player
	 */

	public static void sendMessage(CommandSender sender, String message, Object... args)
	{
		ActionsHandler.addAction(new SendMessageAction(sender, message, args));
	}

	public static void sendMessage(List<CommandSender> senders, String message, Object... args)
	{
		for (CommandSender sender : senders)
			sendMessage(sender, message, args);
	}

	public static void sendMessage(String playerName, String message, Object... args)
	{
		try
		{
			sendMessage(PlayerUtil.getPlayer(playerName), message, args);
		}
		catch (PlayerNotConnectedException ex)
		{
		}
	}

	/*
	 * Broadcast message to all players
	 */

	public static void broadcastMessage(String message, Object... args)
	{
		ActionsHandler.addAction(new BroadcastMessageAction(message, args));
	}
}
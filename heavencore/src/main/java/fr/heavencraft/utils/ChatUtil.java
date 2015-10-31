package fr.heavencraft.utils;

import java.util.List;

import org.bukkit.command.CommandSender;

import fr.heavencraft.async.actions.ActionsHandler;
import fr.heavencraft.async.actions.BroadcastMessageAction;
import fr.heavencraft.async.actions.SendMessageAction;
import fr.heavencraft.heavencore.exceptions.PlayerNotConnectedException;
import fr.heavencraft.heavencore.utils.PlayerUtil;

public class ChatUtil
{
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
package fr.heavencraft.async.actions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BroadcastMessageAction extends AbstractAction
{
	private static final String BEGIN = "{";
	private static final String END = "}";
	private static final String BROADCAST_COLOR = ChatColor.AQUA.toString();
	private static final String BROADCAST_COLOR_H = ChatColor.GREEN.toString();

	private final String message;
	private final Object[] args;

	public BroadcastMessageAction(String message, Object... args)
	{
		this.message = message;
		this.args = args;
	}

	@Override
	public void executeAction()
	{
		Bukkit.broadcastMessage(String.format(BROADCAST_COLOR
				+ message.replace(BEGIN, BROADCAST_COLOR_H).replace(END, BROADCAST_COLOR), args));
	}
}
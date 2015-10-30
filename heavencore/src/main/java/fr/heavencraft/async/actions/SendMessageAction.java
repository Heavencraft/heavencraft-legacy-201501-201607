package fr.heavencraft.async.actions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SendMessageAction extends AbstractAction
{
	private static final String BEGIN = "{";
	private static final String END = "}";
	private static final String ERROR_COLOR = ChatColor.RED.toString();
	private static final String NORMAL_COLOR = ChatColor.GOLD.toString();

	private final CommandSender sender;
	private final String message;
	private final Object[] args;

	public SendMessageAction(CommandSender sender, String message, Object... args)
	{
		this.sender = sender;
		this.message = message;
		this.args = args;
	}

	@Override
	public void executeAction()
	{
		sender.sendMessage(String.format(
				NORMAL_COLOR + message.replace(BEGIN, ERROR_COLOR).replace(END, NORMAL_COLOR), args));
	}
}
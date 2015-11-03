package fr.heavencraft.heavencore.utils.chat;

import org.bukkit.command.CommandSender;

import fr.heavencraft.async.actions.AbstractAction;

class SendMessageAction extends AbstractAction
{
	private static final String BEGIN = "{";
	private static final String END = "}";

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
		String format = ChatUtil.normalColor
				+ message.replace(BEGIN, ChatUtil.errorColor).replace(END, ChatUtil.normalColor);
		sender.sendMessage(String.format(format, args));
	}
}
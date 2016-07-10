package fr.hc.quest.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.quest.HeavenQuest;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import net.citizensnpcs.api.CitizensAPI;

public class RemoveNPCCommand extends AbstractCommandExecutor
{
	public RemoveNPCCommand()
	{
		super(HeavenQuest.get(), "removenpc");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		CitizensAPI.getNPCRegistry().deregisterAll();
		ChatUtil.sendMessage(sender, " NPC removed");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
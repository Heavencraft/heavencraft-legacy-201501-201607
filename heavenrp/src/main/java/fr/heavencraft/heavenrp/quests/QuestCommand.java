package fr.heavencraft.heavenrp.quests;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;

public class QuestCommand extends AbstractCommandExecutor
{
	public QuestCommand(HeavenRP plugin)
	{
		super(plugin, "quetes", RPPermissions.SCROLL_GIVE);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		throw new HeavenException("Uniquement utilisable depuis le jeu.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}

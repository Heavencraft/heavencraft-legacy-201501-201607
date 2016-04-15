package fr.heavencraft.heavenrp.questframework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class QuestCommand extends AbstractCommandExecutor
{
	public QuestCommand(HeavenRP plugin)
	{
		super(plugin, "quetes");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if(player.getName().equalsIgnoreCase("Manu67100"))
		{
			
		}
		
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

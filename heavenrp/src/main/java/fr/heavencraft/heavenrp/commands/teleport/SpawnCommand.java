package fr.heavencraft.heavenrp.commands.teleport;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.worlds.WorldsManager;

public class SpawnCommand extends AbstractCommandExecutor
{
	public SpawnCommand(HeavenRP plugin)
	{
		super(plugin, "spawn");
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		ActionsHandler.addAction(new TeleportPlayerAction(player, WorldsManager.getSpawn())
		{
			@Override
			public void onSuccess()
			{
				ChatUtil.sendMessage(player, "Vous avez été téléporté au spawn.");
			}
		});
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
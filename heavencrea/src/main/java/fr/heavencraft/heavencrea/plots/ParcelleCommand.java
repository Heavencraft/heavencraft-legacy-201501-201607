package fr.heavencraft.heavencrea.plots;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class ParcelleCommand extends AbstractCommandExecutor
{
	public ParcelleCommand(HeavenPlugin plugin)
	{
		super(plugin, "parecelle", null);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}

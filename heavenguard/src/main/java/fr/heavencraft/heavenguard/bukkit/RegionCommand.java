package fr.heavencraft.heavenguard.bukkit;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.bukkit.AbstractBukkitCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenguard.api.RegionProvider;
import fr.heavencraft.heavenguard.bukkit.commands.AddOwnerSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.DefineSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.FlagSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.RedefineSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.RemoveOwnerSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.RemoveSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SelectSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SetparentSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.SubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.AddMemberSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.InfoSubCommand;
import fr.heavencraft.heavenguard.bukkit.commands.owner.RemoveMemberSubCommand;

public class RegionCommand extends AbstractBukkitCommandExecutor
{
	private final Map<String, SubCommand> subCommands = new HashMap<String, SubCommand>();

	public RegionCommand(JavaPlugin plugin, RegionProvider regionProvider)
	{
		super(plugin, "region");

		subCommands.put("define", new DefineSubCommand(regionProvider));
		subCommands.put("redefine", new RedefineSubCommand(regionProvider));
		subCommands.put("select", new SelectSubCommand(regionProvider));
		subCommands.put("info", new InfoSubCommand(regionProvider));
		subCommands.put("setparent", new SetparentSubCommand(regionProvider));
		subCommands.put("remove", new RemoveSubCommand(regionProvider));
		subCommands.put("flag", new FlagSubCommand(regionProvider));

		subCommands.put("addmember", new AddMemberSubCommand(regionProvider));
		subCommands.put("removemember", new RemoveMemberSubCommand(regionProvider));
		subCommands.put("addowner", new AddOwnerSubCommand(regionProvider));
		subCommands.put("removeowner", new RemoveOwnerSubCommand(regionProvider));
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		onConsoleCommand(player, args);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		if (args.length < 2)
		{
			sendUsage(sender);
			return;
		}

		final String command = args[0].toLowerCase();
		final String regionName = args[1].toLowerCase();

		final String[] args2 = new String[args.length - 2];

		if (args2.length != 0)
		{
			for (int i = 0; i != args2.length; i++)
				args2[i] = args[i + 2].toLowerCase();
		}

		final SubCommand subCommand = subCommands.get(command);

		if (subCommand == null)
		{
			sendUsage(sender);
			return;
		}

		if (!subCommand.canExecute(sender, regionName))
			return;

		subCommand.execute(sender, regionName, args2);
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		for (final SubCommand subCommand : subCommands.values())
			if (subCommand.canExecute(sender, null))
				subCommand.sendUsage(sender);
	}
}
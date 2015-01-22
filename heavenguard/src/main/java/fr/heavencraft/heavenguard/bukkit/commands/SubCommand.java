package fr.heavencraft.heavenguard.bukkit.commands;

import org.bukkit.command.CommandSender;

import fr.heavencraft.heavencore.exceptions.HeavenException;

public interface SubCommand
{
	boolean canExecute(CommandSender sender, String regionName);

	void execute(CommandSender sender, String regionName, String[] args) throws HeavenException;

	void sendUsage(CommandSender sender);
}
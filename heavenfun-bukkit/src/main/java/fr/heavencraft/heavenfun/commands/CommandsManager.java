package fr.heavencraft.heavenfun.commands;

import fr.heavencraft.heavencore.bukkit.commands.CreacheatCommand;
import fr.heavencraft.heavencore.bukkit.commands.EndercheatCommand;
import fr.heavencraft.heavencore.bukkit.commands.InventoryCommand;
import fr.heavencraft.heavencore.bukkit.commands.RoucoupsCommand;
import fr.heavencraft.heavencore.bukkit.commands.SpawnCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpCommand;
import fr.heavencraft.heavencore.bukkit.commands.TphereCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpposCommand;
import fr.heavencraft.heavencore.bukkit.commands.TpworldCommand;
import fr.heavencraft.heavenfun.BukkitHeavenFun;
import fr.heavencraft.heavenfun.commands.events.EventCommand;
import fr.heavencraft.heavenfun.economy.FunPointsAdminCommand;
import fr.heavencraft.heavenfun.economy.FunPointsCommand;

public class CommandsManager
{
	public static void init(BukkitHeavenFun plugin)
	{
		new SpawnCommand(plugin, BukkitHeavenFun.getSpawn());

		new RoucoupsCommand(plugin);

		new CreacheatCommand(plugin);
		new EndercheatCommand(plugin);
		new InventoryCommand(plugin);
		new TpCommand(plugin);
		new TphereCommand(plugin);
		new TpposCommand(plugin);
		new TpworldCommand(plugin);

		// Commandes modérateurs
		new EventCommand(plugin);

		new FunPointsCommand(plugin);
		new FunPointsAdminCommand(plugin);
	}
}

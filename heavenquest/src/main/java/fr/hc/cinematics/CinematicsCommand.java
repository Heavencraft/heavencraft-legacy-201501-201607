package fr.hc.cinematics;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CinematicsCommand implements CommandExecutor
{

	private final Cinematics cinematics;
	private int cinematicsQuantity;

	private final String UNAVAILABLE = ChatColor.RED + "Cinematics is not available";
	private final String LAUCHCINEMATICS = ChatColor.GOLD + "/" + ChatColor.RED + "Cinematics " + ChatColor.GOLD
			+ "<number> Pour lancer une cin�matique.";
	private final String LISTCINEMATICS = ChatColor.GOLD + "/" + ChatColor.RED + "Cinematics " + ChatColor.GOLD
			+ "list Pour voir la liste des cin�matiques disponibles.";

	public CinematicsCommand(Cinematics plugin, int cinematicsQuantity)
	{
		cinematics = plugin;
		this.cinematicsQuantity = cinematicsQuantity;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String commandString, String[] args)
	{
		// Test if command format is good
		if (args.length != 1)
		{
			sendUsages(commandSender);
			return false;
		}

		if (args[0].equals("list"))
		{
			sendCinematicsList(commandSender);
			return false;
		}

		// Test if argument is an integer
		Integer cinematicsIndex;
		try
		{
			cinematicsIndex = Integer.valueOf(args[0]);
		}
		catch (NumberFormatException e)
		{
			sendUsages(commandSender);
			return false;
		}

		// Test if the cinematic is available and launch it
		if (cinematics.isCinematicsAvailable(cinematicsIndex))
		{
			try
			{
				Player player = (Player) commandSender;
				CinematicsHandler.lauchCinematics(player, cinematicsIndex, cinematics);
			}
			catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
					| IllegalAccessException e)
			{
				e.printStackTrace();
			}
			return true;
		}

		commandSender.sendMessage(UNAVAILABLE);
		return false;
	}

	private void sendUsages(CommandSender commandSender)
	{
		commandSender.sendMessage(LAUCHCINEMATICS);
		commandSender.sendMessage(LISTCINEMATICS);
	}

	private void sendCinematicsList(CommandSender commandSender)
	{
		for (int index = 0; index < cinematicsQuantity; index++)
		{
			String description = cinematics.getCinematicsDescription(index);
			commandSender.sendMessage(
					ChatColor.GOLD + Integer.toString(index) + ChatColor.RED + " : " + ChatColor.GOLD + description);
		}

	}

}

package fr.hc.cinematics;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.hc.quest.HeavenQuest;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class CinematicsCommand extends AbstractCommandExecutor
{
	private final Cinematics cinematics;
	private int cinematicsQuantity;

	private final String UNAVAILABLE = ChatColor.RED + "Cinematics is not available";
	private final String LAUCHCINEMATICS = ChatColor.GOLD + "/" + ChatColor.RED + "Cinematics " + ChatColor.GOLD
			+ "<number> Pour lancer une cin�matique.";
	private final String LISTCINEMATICS = ChatColor.GOLD + "/" + ChatColor.RED + "Cinematics " + ChatColor.GOLD
			+ "list Pour voir la liste des cin�matiques disponibles.";

	public CinematicsCommand(Cinematics cinematics, HeavenQuest heavenQuest, int cinematicsQuantity)
	{
		super(heavenQuest, "cinematics");
		this.cinematics = cinematics;
		this.cinematicsQuantity = cinematicsQuantity;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		// Test if command format is good
		if (args.length != 1)
			sendUsage(player);

		if (args[0].equals("list"))
			sendCinematicsList(player);

		// Test if argument is an integer
		Integer cinematicsIndex = null;
		try
		{
			cinematicsIndex = Integer.valueOf(args[0]);
		}
		catch (NumberFormatException e)
		{
			sendUsage(player);
		}

		// Test if the cinematic is available and launch it
		if (cinematics.isCinematicsAvailable(cinematicsIndex))
		{
			try
			{
				CinematicsHandler.lauchCinematics(player, cinematicsIndex, cinematics);
			}
			catch (NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException
					| IllegalAccessException e)
			{
				e.printStackTrace();
			}
		}

		player.sendMessage(UNAVAILABLE);
	}

	@Override
	protected void onConsoleCommand(CommandSender arg0, String[] arg1) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender commandSender)
	{
		commandSender.sendMessage(LAUCHCINEMATICS);
		commandSender.sendMessage(LISTCINEMATICS);

	}

	private void sendCinematicsList(Player player)
	{
		for (int index = 0; index < cinematicsQuantity; index++)
		{
			String description = cinematics.getCinematicsDescription(index);
			player.sendMessage(ChatColor.GOLD + Integer.toString(index) + ChatColor.RED + " : " + ChatColor.GOLD
					+ description);
		}

	}
}

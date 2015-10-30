package fr.lorgan17.heavenrp.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.heavencraft.exceptions.HeavenException;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountType;
import fr.heavencraft.heavenrp.database.bankaccounts.BankAccountsManager;
import fr.heavencraft.heavenrp.database.users.User;
import fr.heavencraft.heavenrp.database.users.UserProvider;
import fr.heavencraft.utils.ChatUtil;

public class AuctionManager
{
	private boolean isStarted = false;
	private Location roomLocation;
	private final Map<String, Location> previousLocations = new HashMap<String, Location>();

	private String name = "";
	private int currentPrice;
	String buyerName = "";

	public void create(Player modo, String objectName, int startPrice) throws HeavenException
	{
		if (isStarted)
		{
			throw new HeavenException("Une enchère est déjà en cours.");
		}
		name = objectName;
		currentPrice = startPrice;
		isStarted = true;
		roomLocation = modo.getLocation();

		previousLocations.clear();
		previousLocations.put(modo.getName(), modo.getLocation());

		ChatUtil.broadcastMessage("Les enchères pour {%1$s} viennent de commencer !", new Object[]
		{ objectName });
		ChatUtil.broadcastMessage(
				"La mise à prix est de {%1$d} po ! Faites /enchere entrer pour rejoindre l'enchère.",
				new Object[]
				{ Integer.valueOf(startPrice) });
	}

	public void bid(String playerName, int newPrice) throws HeavenException
	{
		if (!isStarted)
			throw new HeavenException("Aucune enchère n'est en cours !");

		if (!previousLocations.containsKey(playerName))
			throw new HeavenException("Vous n'êtes pas dans la salle d'enchères !");

		if (newPrice <= currentPrice)
			throw new HeavenException(
					"Vous devez miser plus d'argent ! L'enchère est pour le moment à {%1$s} po.", currentPrice);

		if (playerName == buyerName)
			throw new HeavenException("Vous êtes déjà en tête des enchères !");

		User user = UserProvider.getUserByName(playerName);

		if (user == null)
		{
			return;
		}

		int money = user.getBalance();
		int account = BankAccountsManager.getBankAccount(playerName, BankAccountType.USER).getBalance();

		if (money + account < newPrice)
		{
			throw new HeavenException("Vous n'avez pas assez d'argent !");
		}

		buyerName = playerName;
		currentPrice = newPrice;

		broadcast("{" + playerName + "} vient de surencherir avec {" + String.valueOf(newPrice) + "} po !");
	}

	public void stop() throws HeavenException
	{
		if (!isStarted)
		{
			throw new HeavenException("Aucune enchère n'est en cours !");
		}
		for (Entry<String, Location> entry : previousLocations.entrySet())
		{
			Player player = Bukkit.getPlayer(entry.getKey());

			if (player != null)
			{
				player.teleport(entry.getValue());
			}
		}
		previousLocations.clear();
		isStarted = false;

		broadcast("L'enchère vient de se terminer, {" + buyerName + "} a acheté {" + name + "} pour {"
				+ currentPrice + "} po ! Faites /enchere sortir pour sortir de la salle d'enchères.");
	}

	public void enterRoom(Player player) throws HeavenException
	{
		if (!isStarted)
		{
			throw new HeavenException("Aucune enchère n'est en cours !");
		}
		if (previousLocations.containsKey(player.getName()))
		{
			throw new HeavenException("Vous êtes déjà dans la salle d'enchère.");
		}
		previousLocations.put(player.getName(), player.getLocation());
		player.teleport(roomLocation);

		player.sendMessage(ChatColor.AQUA + "[Enchères] " + ChatColor.WHITE
				+ "Vous venez de rentrer dans la salle d'enchères.");
	}

	public void exitRoom(Player player) throws HeavenException
	{
		Location location = previousLocations.remove(player.getName());

		if (location == null)
		{
			throw new HeavenException("Vous n'êtes pas dans la salle d'enchère.");
		}
		player.teleport(location);
		ChatUtil.sendMessage(player, "{[Enchères]} Vous venez de sortie de la salle d'enchères.");
	}

	public void broadcast(String message)
	{
		for (String playerName : previousLocations.keySet())
		{
			Player p = Bukkit.getPlayer(playerName);

			if (p != null)
				p.sendMessage(ChatColor.AQUA
						+ "[Enchères] "
						+ ChatColor.WHITE
						+ message.replace("{", ChatColor.AQUA.toString())
								.replace("}", ChatColor.WHITE.toString()));
		}
	}
}

/*
 * Location: /Users/nao/workspace/heavenrp/target/heavenrp-0.0.1-SNAPSHOT.jar
 * Qualified Name: fr.lorgan17.heavenrp.managers.AuctionManager JD-Core Version:
 * 0.6.2
 */
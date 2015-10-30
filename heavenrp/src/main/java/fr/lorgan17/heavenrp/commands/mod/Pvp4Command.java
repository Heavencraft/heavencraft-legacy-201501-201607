package fr.lorgan17.heavenrp.commands.mod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.selections.Selection;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.PlayerUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.lorgan17.heavenrp.listeners.PVP4Manager;

public class Pvp4Command extends AbstractCommandExecutor
{
	public Pvp4Command(HeavenRP plugin)
	{
		super(plugin, "pvp4", RPPermissions.PVP4);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 0)
		{
			sendUsage(player);
			return;
		}

		if (args[0].equalsIgnoreCase("start"))
		{
			List<Player> players = new ArrayList<Player>();

			for (int i = 1; i != args.length; i++)
				players.add(PlayerUtil.getPlayer(args[i]));

			PVP4Manager.startBattle(players);
		}
		else if (args[0].equalsIgnoreCase("startwe"))
		{
			Selection selection = DevUtil.getWESelection(player);
			List<Player> players = new ArrayList<Player>();

			for (Player player2 : Bukkit.getOnlinePlayers())
				if (selection.contains(player2.getLocation()))
					players.add(player2);

			PVP4Manager.startBattle(players);
		}
		else if (args[0].equalsIgnoreCase("stop"))
		{
			PVP4Manager.stopBattle();
		}
		else if (args[0].equalsIgnoreCase("addspawn"))
		{
			PVP4Manager.addSpawn(player.getLocation());
			ChatUtil.sendMessage(player, "Le point de spawn a été ajouté.");
		}
		else if (args[0].equalsIgnoreCase("resetspawn"))
		{
			PVP4Manager.resetSpawns();
			ChatUtil.sendMessage(player, "Les points de spawn ont été supprimés.");
		}
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "{/pvp4} start <joueur1> <joueur2> etc. : démarre le combat");
		ChatUtil.sendMessage(sender,
				"{/pvp4} startwe : démarre le combat avec les joueurs présent dans la sélection");
		ChatUtil.sendMessage(sender, "{/pvp4} addspawn : ajoute un point de spawn");
		ChatUtil.sendMessage(sender, "{/pvp4} resetspawn : retire tous les points de spawn");
		ChatUtil.sendMessage(sender, "{/pvp4} stop");
	}
}
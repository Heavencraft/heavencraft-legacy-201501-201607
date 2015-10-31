package fr.lorgan17.heavenrp.commands.mod;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.DevUtil;
import fr.heavencraft.heavencore.utils.PlayerUtil;
import fr.heavencraft.heavenrp.HeavenRP;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.utils.ChatUtil;
import fr.lorgan17.heavenrp.listeners.PVPManager;

public class PvpCommand extends AbstractCommandExecutor
{
	public PvpCommand(HeavenRP plugin)
	{
		super(plugin, "pvp", RPPermissions.PVP);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (args.length == 1 && args[0].equalsIgnoreCase("stop"))
		{
			PVPManager.StopBattle();
			return;
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("setspawn"))
		{
			PVPManager.setSpawn(player.getLocation(), args[1]);
			ChatUtil.sendMessage(player, "Le point de spawn a bien été défini.");
			return;
		}

		if (args.length != 3)
		{
			sendUsage(player);
			return;
		}

		List<Player> team1 = new ArrayList<Player>();
		List<Player> team2 = new ArrayList<Player>();

		for (String playerName : args[0].split(","))
			team1.add(PlayerUtil.getPlayer(playerName));

		for (String playerName : args[1].split(","))
			team2.add(PlayerUtil.getPlayer(playerName));

		int maxPoints = DevUtil.toUint(args[2]);

		PVPManager.StartBattle(team1, team2, maxPoints);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		ChatUtil.sendMessage(sender, "duel : {/pvp} joueur1 joueur2 nombreDePointsPourGagner");
		ChatUtil.sendMessage(sender,
				"équipe : {/pvp} joueur1,joueur2,... joueur3,joueur4,... nombreDePointsPourGagner");
		ChatUtil.sendMessage(sender, "spawn : {/pvp} setspawn numéroEquipe");
		ChatUtil.sendMessage(sender, "{/pvp} stop");
	}
}
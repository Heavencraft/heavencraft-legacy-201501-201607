package fr.heavencraft.hellcraft.back;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.hellcraft.worlds.WorldsManager;

public class BackCommand extends AbstractCommandExecutor
{
	public BackCommand(HeavenPlugin plugin)
	{
		super(plugin, "back");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if (!WorldsManager.getWorldSpawn().equals(player.getWorld()))
			throw new HeavenException("Vous devez être au spawn pour utiliser cette commande.");

		final Location deathLocation = BackListener.getDeathLocation(player.getName());

		if (deathLocation == null)
			throw new HeavenException("Vous devez être mort au moins une fois pour utiliser cette commande.");

		plugin.teleportPlayer(player, deathLocation);
		plugin.sendMessage(player, "Vous avez été téléporté à l'endroit où vous étiez mort.");
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
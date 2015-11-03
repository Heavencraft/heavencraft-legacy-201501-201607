package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public final class SpawnCommand extends AbstractCommandExecutor
{
	private static final String SUCCESS_MESSAGE = "Vous avez été téléporté au spawn.";

	private final Location spawnLocation;

	public SpawnCommand(HeavenPlugin plugin, Location spawnLocation)
	{
		super(plugin, "spawn");
		this.spawnLocation = spawnLocation;
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		PlayerUtil.teleportPlayer(player, spawnLocation, SUCCESS_MESSAGE);
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		ChatUtil.sendMessage(sender, "Cette commande n'est pas utilisable depuis la console.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}
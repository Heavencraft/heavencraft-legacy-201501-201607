package fr.heavencraft.heavencore.bukkit.commands;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.chat.ChatUtil;
import fr.heavencraft.heavencore.utils.player.PlayerUtil;

public final class TutoCommand extends AbstractCommandExecutor
{
	private static final String SUCCESS_MESSAGE = "Vous avez été téléporté au tutoriel.";

	private final Location tutoLocation;

	public TutoCommand(HeavenPlugin plugin, Location tutoLocation)
	{
		super(plugin, "tuto");
		this.tutoLocation = tutoLocation;
	}

	@Override
	protected void onPlayerCommand(final Player player, String[] args) throws HeavenException
	{
		PlayerUtil.teleportPlayer(player, tutoLocation, SUCCESS_MESSAGE);
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
package fr.lorgan17.heavenrp.commands.mod;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.commands.HeavenCommand;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.RPPermissions;
import fr.heavencraft.utils.ChatUtil;

public class ModpackCommand extends HeavenCommand
{

	public ModpackCommand()
	{
		super("modpack", RPPermissions.MODPACK);
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		player.getInventory().addItem(new ItemStack(Material.ARROW, 1));
		player.getInventory().addItem(new ItemStack(Material.STICK, 1));
		player.getInventory().addItem(new ItemStack(Material.COMPASS, 1));
		player.getInventory().addItem(new ItemStack(Material.WATCH, 1));

		ChatUtil.sendMessage(player, "Vous venez de percevoir votre paquetage réglementaire.");

	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
		// TODO Auto-generated method stub

	}

}

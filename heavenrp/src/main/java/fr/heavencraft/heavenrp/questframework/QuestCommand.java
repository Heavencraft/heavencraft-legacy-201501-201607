package fr.heavencraft.heavenrp.questframework;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import fr.heavencraft.heavencore.bukkit.commands.AbstractCommandExecutor;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavenrp.HeavenRP;

public class QuestCommand extends AbstractCommandExecutor
{
	public QuestCommand(HeavenRP plugin)
	{
		super(plugin, "quetes");
	}

	@Override
	protected void onPlayerCommand(Player player, String[] args) throws HeavenException
	{
		if(player.getName().equalsIgnoreCase("Manu67100"))
		{
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
			
			ItemMeta meta = book.getItemMeta();
			meta.setDisplayName(QuestBookListener.BOOK_TITLE);
			book.setItemMeta(QuestBookHandler.getInstance().GenerateMeta(player, (BookMeta)book.getItemMeta()));
			player.getInventory().addItem(book);
		}
		
	}

	@Override
	protected void onConsoleCommand(CommandSender sender, String[] args) throws HeavenException
	{
		throw new HeavenException("Uniquement utilisable depuis le jeu.");
	}

	@Override
	protected void sendUsage(CommandSender sender)
	{
	}
}

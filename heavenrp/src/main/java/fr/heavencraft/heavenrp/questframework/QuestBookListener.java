package fr.heavencraft.heavenrp.questframework;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavenrp.HeavenRP;

public class QuestBookListener extends AbstractListener<HeavenPlugin>
{
	public static String BOOK_TITLE = ChatColor.GOLD + "Journal des quêtes";
	public QuestBookListener(HeavenRP plugin)
	{
		super(plugin);
	}

	//TODO select better event for this
	@EventHandler
	public void onQuestBookOpen(PlayerItemHeldEvent event)
	{
		Player p = event.getPlayer();
		ItemStack inHand = p.getInventory().getItem(event.getNewSlot());
		
		if(inHand == null || inHand.getType() != Material.WRITTEN_BOOK)
			return;
		
		
		BookMeta book = (BookMeta)inHand.getItemMeta();
		if(book.hasTitle() && book.getTitle().equalsIgnoreCase(BOOK_TITLE)){
			// Update book
			//inHand.setItemMeta(QuestBookHandler.getInstance().GenerateMeta(p, book));
		}
	}
}

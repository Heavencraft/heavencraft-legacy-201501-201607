package fr.heavencraft.heavencore.utils.menu.options;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;

public class PluginInfoOption extends Option {

	public PluginInfoOption(JavaPlugin plugin, ChatColor highlightColor, ChatColor normalColor, Material type) {
		super(type);
		PluginDescriptionFile pdf = plugin.getDescription();
		
		this.setName(highlightColor + pdf.getName() + normalColor + " version " + highlightColor + pdf.getVersion());
		
		List<String> description = new ArrayList<String>();
		
		if (pdf.getDescription() != null) {
			description.add(normalColor + pdf.getDescription());
		}
		
		if (pdf.getWebsite() != null) {
			description.add(highlightColor + "Website: " + normalColor + pdf.getWebsite());
		}
		
		List<String> authors = pdf.getAuthors();
		if (!authors.isEmpty()) {
			String authorString = highlightColor + "Author" + (authors.size() > 1 ? "s: " : ": ") + normalColor + authors.get(0);
			for (int i = 1; i < authors.size(); i++) {
				if (i + 1 < authors.size()) {
					authorString += highlightColor + ", " + normalColor;
				} else {
					authorString += highlightColor + " and " + normalColor;
				}
				authorString += authors.get(i);
			}
			description.add(authorString);
		}
		this.setDescription(description);
	}

	@Override
	public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) {
		
	}
	
}

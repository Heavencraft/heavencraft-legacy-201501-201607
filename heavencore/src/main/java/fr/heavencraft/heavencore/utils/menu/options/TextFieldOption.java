package fr.heavencraft.heavencore.utils.menu.options;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.utils.menu.TextField;
import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;
import fr.heavencraft.heavencore.utils.menu.MenuAPI;

public class TextFieldOption extends Option {

	private TextField textField;
	
	public TextFieldOption(TextField textField, Material type) {
		super(type);
		this.setTextField(textField);
	}
	
	public TextFieldOption(TextField textField, Material type, String name, String... description) {
		super(type, name, description);
		this.setTextField(textField);
	}
	
	public TextFieldOption(TextField textField, Material type, short damage, String name, String... description) {
		super(type, damage, name, description);
		this.setTextField(textField);
	}
	
	public final TextFieldOption setTextField(TextField textField) {
		this.textField = textField;
		return this;
	}
	
	public final TextField getTextField() {
		return this.textField;
	}

	@Override
	public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException {
		if (!MenuAPI.openTextField(player, this.textField)) {
			MenuAPI.openMenu(player, menu);
		}
	}
	
}

package fr.heavencraft.heavencore.utils.menu.options;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.heavencraft.heavencore.exceptions.HeavenException;
import fr.heavencraft.heavencore.utils.menu.ClickType;
import fr.heavencraft.heavencore.utils.menu.Menu;


public class CountOption extends Option {

	private boolean checkMaxStackSize = false;
	
	private int minCount = 1;
	private int maxCount = 64;
	
	public CountOption(Material type) {
		super(type);
	}
	
	public CountOption(Material type, String name, String... description) {
		super(type, name, description);
	}
	
	public CountOption(Material type, short damage, String name, String... description) {
		super(type, damage, name, description);
	}
	
	public final CountOption setCheckMaxStackSize(boolean check) {
		this.checkMaxStackSize = check;
		return this;
	}
	
	public final boolean getCheckMaxStackSize() {
		return this.checkMaxStackSize;
	}

	public final CountOption setMinCount(int count) {
		this.minCount = count < 1 ? 1 : count;
		return this;
	}
	
	public final int getMinCount() {
		return this.minCount;
	}
	
	public final CountOption setMaxCount(int count) {
		this.maxCount = count > 64 ? count : 64;
		return this;
	}
	
	public final int getMaxCount() {
		return this.maxCount;
	}
	
	@Override
	public void onClick(Menu menu, Player player, ItemStack cursor, ItemStack current, ClickType type) throws HeavenException {
		int amount = this.getAmount();
		if (type == ClickType.LEFT) {
			amount = amount + 1;
		} else if (type == ClickType.SHIFT_LEFT) {
			amount = this.getMaxCount();
		} else if (type == ClickType.RIGHT) {
			amount = amount - 1;
		} else if (type == ClickType.SHIFT_RIGHT) {
			amount = this.getMinCount();
		}
		if (this.getCheckMaxStackSize()) {
			int maxAmount = current.getType().getMaxStackSize();
			if (amount > maxAmount) {
				amount = maxAmount;
			}
		}
		if (amount <= 0) {
			amount = 1;
		}
		this.setAmount(amount);
		menu.refresh(player);
	}

}

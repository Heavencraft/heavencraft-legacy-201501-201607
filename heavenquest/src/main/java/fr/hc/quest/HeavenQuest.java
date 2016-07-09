package fr.hc.quest;

import fr.hc.scrolls.ScrollCommand;
import fr.hc.scrolls.ScrollListener;
import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class HeavenQuest extends HeavenPlugin
{
	private static HeavenQuest _instance;
	
	/**
	 * Returns an instance of Heaven Quest
	 * @return
	 */
	public static HeavenQuest get() {
		return _instance;
	}
	
	@Override
	public void onEnable()
	{
		super.onEnable();
		HeavenQuest._instance = this;
		new CreateNPCommand(this);
		new ScrollCommand(this);
		new ScrollListener(this);
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcListener.class).withName("NpcListener"));
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}
}

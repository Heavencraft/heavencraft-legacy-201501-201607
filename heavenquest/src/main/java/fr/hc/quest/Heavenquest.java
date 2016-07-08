package fr.hc.quest;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class Heavenquest extends HeavenPlugin
{
	@Override
	public void onEnable()
	{
		super.onEnable();
		new CreateNPCommand(this);
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcListener.class).withName("NpcListener"));
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}
}

package fr.hc.quest;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;

public class Heavenquest extends HeavenPlugin
{
	private static Heavenquest instance;

	public Heavenquest()
	{
		instance = this;
	}

	@Override
	public void onEnable()
	{
		CitizensAPI.getNPCRegistry().deregisterAll();

		super.onEnable();
		new CreateNPCommand(this);
		new TestCommand(this);
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(NpcListener.class).withName("NpcListener"));
	}

	@Override
	public void onDisable()
	{
		super.onDisable();
	}

	public static Heavenquest getInstance()
	{
		return instance;
	}
}

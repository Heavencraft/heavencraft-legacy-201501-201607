package fr.hc.quest;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitInfo;

public class AbstractHeavenTrait extends Trait
{

	public AbstractHeavenTrait(String name, Class classe)
	{
		super(name);
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(classe));
	}

}

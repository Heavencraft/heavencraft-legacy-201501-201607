package fr.heavencraft.heavenvip.vipeffects;

import fr.heavencraft.heavencore.utils.particles.ParticleEffect;

public class AppliedEffectProperty {
	private final ParticleEffect effect;
	private final int effectAmount;
	private final ParticleEffect.OrdinaryColor oc;
	private final ParticleEffect.NoteColor nc;
	
	/**
	 * 
	 * @param effect What kind of effect
	 * @param effectAmount How many particles
	 */
	public AppliedEffectProperty(ParticleEffect effect, int effectAmount) {
		this.effect = effect;
		this.effectAmount = effectAmount;
		this.oc = null;
		this.nc = null;
	}
	
	public AppliedEffectProperty(ParticleEffect effect, int effectAmount, ParticleEffect.OrdinaryColor oc) {
		this.effect = effect;
		this.effectAmount = effectAmount;
		this.oc = oc;
		this.nc = null;
	}
	
	public AppliedEffectProperty(ParticleEffect effect, int effectAmount, ParticleEffect.NoteColor nc) {
		this.effect = effect;
		this.effectAmount = effectAmount;
		this.oc = null;
		this.nc = nc;
	}
	
	public ParticleEffect getEffect()
	{
		return this.effect;
	}
	
	public int getAmount()
	{
		return this.effectAmount;
	}

	public ParticleEffect.OrdinaryColor getOrdinaryColor()
	{
		return oc;
	}

	public ParticleEffect.NoteColor getNoteColor()
	{
		return nc;
	}
	
	
	
}
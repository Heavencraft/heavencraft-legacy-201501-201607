package fr.heavencraft.heavenvip.movments;

import fr.heavencraft.heavencore.utils.particles.ParticleEffect;

public class AppliedDescriptorProperties {
	private final ParticleEffect effect;
	private final int effectAmount;
	private final float speed;
	private final ParticleEffect.OrdinaryColor oc;
	private final ParticleEffect.NoteColor nc;
	
	/**
	 * 
	 * @param effect What kind of effect
	 * @param effectAmount How many particles
	 */
	public AppliedDescriptorProperties(ParticleEffect effect, int effectAmount, float speed) {
		this.effect = effect;
		this.effectAmount = effectAmount;
		this.speed = speed;
		this.oc = null;
		this.nc = null;
	}
	
	public AppliedDescriptorProperties(ParticleEffect effect, int effectAmount, ParticleEffect.OrdinaryColor oc) {
		this.effect = effect;
		this.effectAmount = effectAmount;
		this.speed = 0f;
		this.oc = oc;
		this.nc = null;
	}
	
	public AppliedDescriptorProperties(ParticleEffect effect, int effectAmount, ParticleEffect.NoteColor nc) {
		this.effect = effect;
		this.effectAmount = effectAmount;
		this.speed = 0f;
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

	public float getSpeed()
	{
		return speed;
	}
	
	
	
}
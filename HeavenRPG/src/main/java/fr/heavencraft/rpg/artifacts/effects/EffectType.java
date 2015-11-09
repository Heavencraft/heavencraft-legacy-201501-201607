package fr.heavencraft.rpg.artifacts.effects;

/**
 * Type of effect : each subclass need to have an entry in this enum. Note: the text is displayed in the inventory.
 */
public enum EffectType
{
	THUNDER_MOBS_3("Eclairs sur tous les mobs dans un rayon de 3."),
	SMALL_DAMAGE_MOBS_3("test");

	private final String _text;

	private EffectType(String text)
	{
		_text = text;
	}

	@Override
	public String toString()
	{
		return _text;
	}
}
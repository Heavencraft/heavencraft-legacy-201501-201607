package fr.heavencraft.rpg.artifacts;

public enum ArtifactType
{
	PIKACHU_BONE("Os de pikachu");

	private final String _text;

	private ArtifactType(String text)
	{
		_text = text;
	}

	@Override
	public String toString()
	{
		return _text;
	}
}
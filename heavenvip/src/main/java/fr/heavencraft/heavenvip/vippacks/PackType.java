package fr.heavencraft.heavenvip.vippacks;

public enum PackType
{
	PARTICLE('p', "particle"),
	HEAD('h', "head");
	
	private final char type;
	private PackType(char type, String desc) {
		this.type = type;
	}
	
	public char getPackType()
	{
		return type;
	}
}

package fr.heavencraft.heavenrp.dungeon;

public class Dungeon
{
	public Dungeon(int id, String name, int requiredPlayer)
	{
		super();
		this.id = id;
		this.name = name;
		this.requiredPlayer = requiredPlayer;
	}
	public int getId()
	{
		return id;
	}
	public String getName()
	{
		return name;
	}
	public int getRequiredPlayer()
	{
		return requiredPlayer;
	}
	private int id;
	private String name;
	private int requiredPlayer = 1;
	
}

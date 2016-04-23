package fr.heavencraft.heavenvip.heads;

public class Head {
	private final int _price;
	private final String _playerName;
	private final String _description;

	public Head(int price, String playerName, String description) {
		this._price = price;
		this._playerName = playerName;
		this._description = description;
	}

	public int getPrice()
	{
		return _price;
	}

	public String getPlayerName()
	{
		return _playerName;
	}

	public String getDescription()
	{
		return _description;
	}
}
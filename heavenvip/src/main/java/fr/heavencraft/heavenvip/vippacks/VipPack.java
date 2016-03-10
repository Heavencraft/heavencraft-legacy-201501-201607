package fr.heavencraft.heavenvip.vippacks;

public class VipPack
{
	private final int vipPackId;
	private final PackType packType;
	private final String packName;
	private final String description;
	private final int monthOfAvailability;
	private final int price;
	private final boolean owned;
	
	/**
	 * Constructor
	 * @param vipPackId
	 * @param packType
	 * @param packName
	 * @param description
	 * @param monthOfAvailability
	 * @param price
	 * @param owned
	 */
	public VipPack(int vipPackId, PackType packType, String packName, String description, int monthOfAvailability,
			int price, boolean owned)
	{
		this.vipPackId = vipPackId;
		this.packType = packType;
		this.packName = packName;
		this.description = description;
		this.monthOfAvailability = monthOfAvailability;
		this.price = price;
		this.owned = owned;
	}
	
	public int getVipPackId()
	{
		return vipPackId;
	}

	public PackType getPackType()
	{
		return packType;
	}

	public String getPackName()
	{
		return packName;
	}

	public String getDescription()
	{
		return description;
	}

	public int getMonthOfAvailability()
	{
		return monthOfAvailability;
	}

	public int getPrice()
	{
		return price;
	}

	public boolean isOwned()
	{
		return owned;
	}
	
}

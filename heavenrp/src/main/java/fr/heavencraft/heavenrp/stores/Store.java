package fr.heavencraft.heavenrp.stores;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import fr.heavencraft.Utils;
import fr.heavencraft.heavenrp.HeavenRP;

public class Store
{
	private String _ownerName;
	private String _storeName;
	private Sign _sign;
	private Stock _linkedStock;
	
	private int _price;
	private int _quantity;
	private Material _material;
	private int _materialData;

	private boolean _isBuyer;
	
	private boolean _isValid;
	
	public Store(String ownerName, String storeName, Sign sign, Stock linkedStock, int price, int quantity, Material material, int materialData, boolean isBuyer)
	{
		_ownerName = ownerName;
		_storeName = storeName;
		_sign = sign;
		_linkedStock = linkedStock;
		
		_price = price;
		_quantity = quantity;
		_material = material;
		_materialData = materialData;
		
		_isBuyer = isBuyer;
		_isValid = true;
	}
	
	public Store(HeavenRP plugin, String line)
	{
		_isValid = false;
		String[] lineData = line.split(";");
		
		if (lineData.length != 6 && lineData.length != 7)
			return;
		
		_ownerName = lineData[0];
		_storeName = lineData[1];
		_linkedStock = plugin.getStoresManager().getStock(_ownerName, _storeName);
		
		if (_linkedStock == null)
			return;
		
		Block block = Utils.stringToBlock(lineData[2]);
		
		if (block == null || !(block.getState() instanceof Sign))
			return;

		_sign = (Sign) block.getState();
		
		if (!Utils.isNumeric(lineData[3], lineData[4]))
			return;

		_price = Integer.parseInt(lineData[3]);
		_quantity = Integer.parseInt(lineData[4]);
		
		if (_price < 1 || _price > 999 || _quantity < 1 || _quantity > 64)
			return;
		
		String materialId = "";
		String materialInfo = "";
		if (lineData[5].contains(":"))
		{
			String[] materialData = lineData[5].split(":");
			materialId = materialData[0];
			materialInfo = materialData[1];
		}
		else
		{
			materialId = lineData[5];
		}
		
		if (materialId.isEmpty())
			return;
		
		_material = Material.getMaterial(Integer.parseInt(materialId));
		if (_material == null)
			return;
		
		if (!materialInfo.isEmpty())
			_materialData = Integer.parseInt(materialInfo);
		else
			_materialData = -1;
		
		_isBuyer = false;
		if (lineData.length == 7)
			_isBuyer = lineData[6].equals("1");
		
		_isValid = true;
	}

	public boolean isValid()
	{
		return _isValid;
	}
	
	public boolean isBuyer()
	{
		return _isBuyer;
	}
	
	public String getOwnerName()
	{
		return _ownerName;
	}
	
	public String getStoreName()
	{
		return _storeName;
	}
	
	public Sign getSign()
	{
		return _sign;
	}
	
	public Stock getLinkedStock()
	{
		return _linkedStock;
	}

	public int getPrice()
	{
		return _price;
	}

	public int getQuantity()
	{
		return _quantity;
	}
	
	public Material getMaterial()
	{
		return _material;
	}
	
	public int getMaterialData()
	{
		return _materialData;
	}
	
	private String getMaterialString()
	{
		return _material.getId() + ":" + _materialData;
	}
	
	public String getSaveString()
	{
		return _ownerName + ";" + _storeName + ";" +
			Utils.blockToString(_sign.getBlock()) + ";" +
			_price + ";" + _quantity + ";" + getMaterialString() + ";" + (_isBuyer ? "1" : "0");
	}
}
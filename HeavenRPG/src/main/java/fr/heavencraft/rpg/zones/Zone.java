package fr.heavencraft.rpg.zones;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import fr.heavencraft.Utils.DevUtils;
import fr.heavencraft.rpg.RPGFiles;

public class Zone {

	private String _UniqueName;
	private String _name;
	private int _zoneLevel;
	private CuboidSelection _cubo;

	/**
	 * Constructeur, pour charger une zone depuis la config
	 * @param name
	 */
	public Zone(String name)
	{
		this.set_name(name);
		this._UniqueName = name;
		this.set_zoneLevel(RPGFiles.getZones().getInt("Zones." + this._UniqueName + ".level"));
	}

	/**
	 * Constructeur pour créer une zone dans la config.
	 * @param name
	 * @param level
	 * @param cubo
	 */
	public Zone(String name, int level, CuboidSelection cubo)
	{
		this._UniqueName = name;
		this.setName(name);
		this.setZoneLevel(level);
		this.setCubo(cubo);
		
		RPGFiles.saveZones();
	}


	public String getUniqueName() {
		return _UniqueName;
	}

	public String getName() {
		return get_name();
	}

	public void setName(String _name) {
		RPGFiles.getZones().set("Zones." + this._UniqueName + ".name", _name);
		this.set_name(_name);
		RPGFiles.saveZones();
	}

	public int getZoneLevel() {
		return get_zoneLevel();
	}
	public void setZoneLevel(int level) {
		RPGFiles.getZones().set("Zones." + this._UniqueName + ".level", level);
		this.set_zoneLevel(level);
		RPGFiles.saveZones();
	}

	public CuboidSelection getCubo() {
		return get_cubo();
	}

	public void setCubo(CuboidSelection _cubo) {
		this.set_cubo(_cubo);
		RPGFiles.getZones().set("Zones." + this._UniqueName + ".l1",  DevUtils.serializeLoc(_cubo.getMinimumPoint()));
		RPGFiles.getZones().set("Zones." + this._UniqueName + ".l2", DevUtils.serializeLoc(_cubo.getMaximumPoint()));
		RPGFiles.saveZones();
	}

	public int get_zoneLevel() {
		return _zoneLevel;
	}

	public void set_zoneLevel(int _zoneLevel) {
		this._zoneLevel = _zoneLevel;
	}

	public String get_name() {
		return _name;
	}

	public void set_name(String _name) {
		this._name = _name;
	}

	public CuboidSelection get_cubo() {
		return _cubo;
	}

	public void set_cubo(CuboidSelection _cubo) {
		this._cubo = _cubo;
	}

}
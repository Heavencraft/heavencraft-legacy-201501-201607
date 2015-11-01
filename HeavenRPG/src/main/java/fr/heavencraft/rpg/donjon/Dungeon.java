package fr.heavencraft.rpg.donjon;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import fr.heavencraft.Utils.ChatUtil;
import fr.heavencraft.Utils.DevUtils;
import fr.heavencraft.rpg.HeavenRPG;
import fr.heavencraft.rpg.RPGFiles;

public class Dungeon {

	private String _name;
	private boolean _Running = false;
	private Location _lobby;
	private int _requiredPlayerAmmount = 1;

	private HashMap<UUID, Location> _inDungeon = new HashMap<UUID, Location>();
	private List<UUID> _deadPlayers = new ArrayList<UUID>();
	private List<DungeonRoom> _rooms = new ArrayList<DungeonRoom>();

	private int _actualRoom = 0;

	private final static String DUNGEON_DOES_NOT_EXIST = "Ce donjon n'existe pas!";
	private final static String DUNGEON_PLAYER_ALREADY_INSIDE = "Vous ètes déjà dans ce donjon!";
	private final static String DUNGEON_REQUIRE_TRIGGER = "Aucun TRIGGER trouvé, merci de le définir!";
	private final static String DUNGEON_NEED_MORE_PLAYER = "Il manque {%1$s} joueur(s)!";
	private final static String DUNGEON_X_MOBS_LEFT = "Vous devez tuer encore {%1$s} monstre(s)!";
	private final static String DUNGEON_ALREADY_IN_USE = "Ce donjon est en utilisation!";

	static class DungeonRoom
	{
		int _index;
		Location _spawn;
		List<Entity> _mobs = new ArrayList<Entity>();
		CuboidSelection _region;
		Location _trigger;

		public DungeonRoom(int indx, Location loc, CuboidSelection cubo, Location trig)
		{
			set_index(indx);
			set_spawn(loc);
			set_region(cubo);
			set_trigger(trig);
		}
		public DungeonRoom(int indx, Location loc, CuboidSelection cubo)
		{
			set_index(indx);
			set_spawn(loc);
			set_region(cubo);
			set_trigger(null);
		}

		public int get_index()
		{
			return _index;
		}

		public void set_index(int idx)
		{
			_index = idx;
		}

		public void add_mob(Entity ety)
		{
			_mobs.add(ety);
		}

		public void remove_mob(Entity ety)
		{
			_mobs.remove(ety);
		}

		public List<Entity> get_mobs()
		{
			return _mobs;
		}


		public Location get_spawn()
		{
			return _spawn;
		}

		public void set_spawn(Location loc)
		{
			_spawn = loc;
		}

		public CuboidSelection get_region() {
			return _region;
		}
		public void set_region(CuboidSelection _region) {
			this._region = _region;
		}

		public Location get_trigger()
		{
			return _trigger;
		}

		public void set_trigger(Location loc)
		{
			_trigger = loc;
		}

	}

	public Dungeon(String name, int reqUser)
	{
		set_name(name);
		set_requiredPlayerAmmount(reqUser);
		RPGFiles.getDungeons().set("Dungeons." + name + ".requiredPlayers", reqUser);
		RPGFiles.saveDungeons();
	}

	public Dungeon(String name, int reqUser, Location lobby)
	{
		set_name(name);
		set_requiredPlayerAmmount(reqUser);
		set_lobby(lobby);
	}

	public void addDungeonRoom(DungeonRoom dgr)
	{
		_rooms.add(dgr);
	}

	public void removeDungeonRoom(DungeonRoom dgr)
	{
		_rooms.remove(dgr);
	}

	public void updateDungeonRoomSpawn(DungeonRoom dgr, Location spawn)
	{
		dgr.set_spawn(spawn);
	}

	public void handleJoinAttemp(Player p)
	{
		if(is_Running())
		{
			ChatUtil.sendMessage(p, DUNGEON_ALREADY_IN_USE);
			return;
		}
		
		if(isPlayerInside(p))
		{
			ChatUtil.sendMessage(p, DUNGEON_PLAYER_ALREADY_INSIDE);
			return;
		}
			
		// Ajouter le joueur a la liste d'attente
		get_inDungeon().put(p.getUniqueId(), p.getLocation());
		// Téléporter le joueur dans la lobby
		p.teleport(get_lobby());

		// Véifier si le nombre de joueurs est suffisant
		if(get_inDungeon().size() == get_requiredPlayerAmmount())
			startDungeon();		// Démarrer la lobby
		else
			for(UUID uid : get_inDungeon().keySet())
				if(Bukkit.getOfflinePlayer(uid) != null && Bukkit.getOfflinePlayer(uid).isOnline())
					ChatUtil.sendMessage(Bukkit.getPlayer(uid), DUNGEON_NEED_MORE_PLAYER, get_requiredPlayerAmmount() - get_inDungeon().size());

	}

	/**
	 * Starts the dungeon
	 * - Sets: running = true, actualRoom = 1
	 * - Move players to room
	 */
	private void startDungeon()
	{
		_deadPlayers.clear();
		// Marquer le donjon comme en jeu
		set_Running(true);
		// Faire pointer le pointeur du room sur le premier room
		setActualRoom(1);
		// Teleport players to next room
		DungeonRoom dgr = getDungeonRoomByIndex(1);
		// Run the trigger
		handleTrigger(1);

		for(UUID uid : _inDungeon.keySet())
			if(Bukkit.getOfflinePlayer(uid) != null && Bukkit.getOfflinePlayer(uid).isOnline())
				Bukkit.getPlayer(uid).teleport(dgr.get_spawn());
	}

	/**
	 * Tries to move player to next room.
	 * @param p Player who tried.
	 * @param idx Next room index.
	 */
	public void handleChangeRoomAttemp(Player p, int idx)
	{
		DungeonRoom dgr = getDungeonRoomByIndex(getActualRoom());
		DungeonRoom next_dgr = getDungeonRoomByIndex(idx);

		if(dgr == null)
			return;
		
		List<Entity> tmp = dgr.get_mobs();
		for(int i = 0; i < tmp.size(); i++)
		{
			if(tmp.get(i).isDead())
				dgr.get_mobs().remove(i);
		}
		
		// Check if all mobs are dead
		if(dgr.get_mobs().size() != 0)
		{		
			ChatUtil.sendMessage(p, DUNGEON_X_MOBS_LEFT, dgr.get_mobs().size());
			return;
		}

		// Change pointer
		setActualRoom(idx);
		// Activer le bloc trigger sur le nouveau room 
		handleTrigger(idx);

		//Retirer les morts de la liste
		_deadPlayers.clear();
		
		// Teleport players to next room
		for(UUID uid : _inDungeon.keySet())
			if(Bukkit.getOfflinePlayer(uid) != null && Bukkit.getOfflinePlayer(uid).isOnline())
				Bukkit.getPlayer(uid).teleport(next_dgr.get_spawn());
	}

	public void handleTrigger(int idx)
	{
		DungeonRoom dgr = getDungeonRoomByIndex(idx);
		if(dgr.get_trigger() == null)
		{
			DevUtils.log(DUNGEON_REQUIRE_TRIGGER);
			return;
		}
		for(Entity mob : dgr.get_mobs())
			mob.remove();
		dgr._mobs.clear();

		Block redstoneBlock = dgr.get_trigger().getBlock();
		Bukkit.getServer()
		.getScheduler()
		.runTaskLater(
				HeavenRPG.getInstance(),
				new RestoreBlockTask(redstoneBlock.getWorld().getName(), redstoneBlock.getX(), redstoneBlock
						.getY(), redstoneBlock.getZ(), redstoneBlock.getType()), 40);

		redstoneBlock.setType(Material.REDSTONE_BLOCK);
	}

	/**
	 * Handles the try to end the dungeon.
	 * @param p Player who tried.
	 */
	public void handleEndDungeonAttemp(Player p)
	{
		DungeonRoom dgr = getDungeonRoomByIndex(getActualRoom());
		if(dgr == null)
		{
			ChatUtil.sendMessage(p, DUNGEON_DOES_NOT_EXIST);
			return;
		}

		// Check if all mobs are dead
		if(dgr.get_mobs().size() != 0)
		{
			ChatUtil.sendMessage(p, DUNGEON_X_MOBS_LEFT, dgr.get_mobs().size());
			return;
		}
		stopDungeon();
	}

	public void stopDungeon()
	{
		// Téléporter les joueurs a leur point d'entrée
		for(UUID uid : _inDungeon.keySet())
			if(Bukkit.getOfflinePlayer(uid) != null && Bukkit.getOfflinePlayer(uid).isOnline())
				Bukkit.getPlayer(uid).teleport(_inDungeon.get(uid));		

		// Vider la liste des joueurs
		_inDungeon.clear();
		
		//Destroy Entities
		for(DungeonRoom dgr : _rooms)
		{
			for(Entity mob : dgr.get_mobs())
				mob.remove();
			dgr._mobs.clear();
		}

		// Réninitialiser le pointeur
		setActualRoom(0);
		// Marquer le donjon comme en jeu
		set_Running(false);
	}

	
	/**
	 * Handles the death of a player into a dungeon.
	 * Clears his inventory, armor, food, hunger.
	 * @param p Player who died.
	 */
	public void handlePlayerDeath(Player p)
	{
		// Check if player is inside a dungeon.
		if(!isPlayerInside(p)) 
			return;
		// Reset his life, food & effects
		p.setFireTicks(0);
		p.setFoodLevel(20);
		p.setHealth(20);
		for(PotionEffect eff : p.getActivePotionEffects())
			p.removePotionEffect(eff.getType());
		// Remove inventory
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		// Move him to lobby
		p.teleport(get_lobby());
		// Add a reference to dead Players list
		if(!_deadPlayers.contains(p.getUniqueId()))
			_deadPlayers.add(p.getUniqueId());
		
		// If there is no survivor left, stop dungeon.
		if(_deadPlayers.size() >= _inDungeon.size())
			stopDungeon();
		return;
	}
	
	public void handlePlayerDisconnect(Player p)
	{
		// Check if player is inside a dungeon.
		if(!isPlayerInside(p))
			return;
		// Reset his life, food & effects
		p.setFireTicks(0);
		p.setFoodLevel(20);
		p.setHealth(20);
		for(PotionEffect eff : p.getActivePotionEffects())
			p.removePotionEffect(eff.getType());	
		
		if(is_Running())
		{
			// Move him to entry point
			p.teleport(_inDungeon.get(p.getUniqueId()));
			
			// Add a reference to dead Players list
			if(!_deadPlayers.contains(p.getUniqueId()))
				_deadPlayers.add(p.getUniqueId());
			if(_deadPlayers.size() >= _inDungeon.size())
				stopDungeon();
		}
		else 
		{
			// Move him out
			p.teleport(_inDungeon.get(p.getUniqueId()));
			_inDungeon.remove(p.getUniqueId());
		}
		return;
	}
	
	
	public DungeonRoom getDungeonRoomByIndex(int index)
	{
		for(DungeonRoom dgr : get_rooms())
			if(dgr.get_index() == index)
				return dgr;
		return null;
	}

	public int getLastDungeonIndex()
	{
		int result = 0;
		for(DungeonRoom dgr : get_rooms())
			if(dgr.get_index() > result)
				result = dgr.get_index();
		return result;
	}

	public boolean isPlayerInside(Player p)
	{
		if(_inDungeon.containsKey(p.getUniqueId()))
			return true;
		return false;
	}

	public boolean hasRoomWithIndex(int idx)
	{
		for(DungeonRoom dgr : get_rooms())
			if(dgr.get_index() == idx)
				return true;
		return false;
	}

	/**
	 * Returns the name of the dungeon
	 * @return
	 */
	public String get_name() {
		return _name;
	}
	private void set_name(String name) {
		_name = name;
	}

	public boolean is_Running() {
		return _Running;
	}

	private void set_Running(boolean isRunning) {
		_Running = isRunning;
	}

	private Location get_lobby() {
		return _lobby;
	}

	public void set_lobby(Location lobby) {
		_lobby = lobby;
	}

	public int get_requiredPlayerAmmount() {
		return _requiredPlayerAmmount;
	}

	private void set_requiredPlayerAmmount(int requiredPlayerAmmount) {
		_requiredPlayerAmmount = requiredPlayerAmmount;
	}

	private AbstractMap<UUID,Location> get_inDungeon() {
		return _inDungeon;
	}

	public List<DungeonRoom> get_rooms() {
		return _rooms;
	}

	public int getActualRoom() {
		return _actualRoom;
	}

	private void setActualRoom(int actualRoom) {
		_actualRoom = actualRoom;
	}


	class RestoreBlockTask extends BukkitRunnable
	{
		String _world;
		int _x;
		int _y;
		int _z;
		Material _type;

		public RestoreBlockTask(String world, int x, int y, int z, Material type)
		{
			_world = world;
			_x = x;
			_y = y;
			_z = z;
			_type = type;
		}

		@Override
		public void run()
		{
			Bukkit.getServer().getWorld(_world).getBlockAt(_x, _y, _z).setType(_type);
		}
	}
}

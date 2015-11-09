package fr.lorgan17.heavenrp.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import fr.heavencraft.heavencore.bukkit.HeavenPlugin;
import fr.heavencraft.heavencore.bukkit.listeners.AbstractListener;
import fr.heavencraft.heavencore.exceptions.HeavenException;

public class PVPManager extends AbstractListener<HeavenPlugin>
{
	static Location spawn1;
	static Location spawn2;
	private static Team _team1;
	private static Team _team2;
	private static boolean isFighting = false;
	private static boolean isTeamBattle = false;
	static int maxpoints = 0;

	public PVPManager(HeavenPlugin plugin)
	{
		super(plugin);
	}

	public static void StartBattle(List<Player> team1, List<Player> team2, int maxPoints) throws HeavenException
	{
		if (isFighting)
			throw new HeavenException("Un combat est déjà en cours !");

		if (team1.size() == 0)
			throw new HeavenException("Il n'y a pas de joueurs dans l'équipe 1 !?");

		if (team2.size() == 0)
			throw new HeavenException("Il n'y a pas de joueurs dans l'équipe 2 !?");

		_team1 = new Team(team1);
		_team2 = new Team(team2);

		maxpoints = maxPoints;
		isTeamBattle = _team1.isTeam() || _team1.isTeam();
		isFighting = true;
		goSpawn();

		if (isTeamBattle)
		{
			broadcastMessage("C'est l'heure du du-du-du-duelllll !");
			broadcastMessage("%1$s : %2$s", _team1.getName(), _team1.getPlayers());
			broadcastMessage("%1$s : %2$s", _team2.getName(), _team2.getPlayers());
		}
		else
			broadcastMessage("* Le combat entre %1$s et %2$s vient de commencer !", _team1.getName(),
					_team2.getName());
	}

	public static void StopBattle()
	{
		if (isFighting)
		{
			isFighting = false;

			_team1 = null;
			_team2 = null;

			isTeamBattle = false;
			maxpoints = 0;

			broadcastMessage("Le combat vient d'être interrompu !");
		}
	}

	public static void setSpawn(Location l, String team) throws HeavenException
	{
		if (team.equalsIgnoreCase("1"))
			spawn1 = l;
		else if (team.equalsIgnoreCase("2"))
			spawn2 = l;
		else
			throw new HeavenException("L'équipe %1$s n'existe pas.", team);
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e)
	{
		if (!isFighting)
			return;

		Player p = e.getEntity();

		if (_team1.contains(p))
		{
			broadcastMessage("%1$s de %2$s vient de mourir !", p.getName(), _team1.getName());
			_team1.addDead();

			if (_team1.hasLost())
			{
				broadcastMessage("%1$s a gagné la manche !", _team2.getName());
				_team1.resetDead();
				_team2.resetDead();
				_team2.addPoint();
				goSpawn();
			}
		}

		else if (_team2.contains(p))
		{
			broadcastMessage("%1$s de %2$s vient de mourir !", p.getName(), _team2.getName());
			_team2.addDead();

			if (_team2.hasLost())
			{
				broadcastMessage("%1$s a gagné la manche !", _team1.getName());
				_team1.resetDead();
				_team2.resetDead();
				_team1.addPoint();
				goSpawn();
			}
		}

		else
			return;

		e.setKeepLevel(true);

		broadcastMessage("%1$s %2$d - %3$d %4$s", _team1.getName(), _team1.getPoints(), _team2.getPoints(),
				_team2.getName());

		if (_team1.getPoints() == maxpoints)
			broadcastMessage("{%1$s} a gagné le duel !", _team1.getName());
		else if (_team2.getPoints() == maxpoints)
			broadcastMessage("{%1$s} a gagné le duel !", _team2.getName());
		else
			return;

		_team1 = null;
		_team2 = null;
		isTeamBattle = false;
		isFighting = false;
		maxpoints = 0;
	}

	private static void goSpawn()
	{
		if (Math.random() > 0.5)
		{
			_team1.spawn(spawn1);
			_team2.spawn(spawn2);
		}
		else
		{
			_team1.spawn(spawn2);
			_team2.spawn(spawn1);
		}

	}

	private static void broadcastMessage(String message, Object... args)
	{
		Bukkit.broadcastMessage(ChatColor.AQUA + " * " + String.format(message, args));
	}

	static class Team
	{
		private String _teamname;
		private String _list;
		private int dead;
		private int points;
		private final List<Player> _players;

		public Team(List<Player> players) throws HeavenException
		{
			_players = players;

			if (players.size() == 1)
				_teamname = players.get(0).getName();
			else
				_teamname = "L'équipe " + players.get(0).getName();

			_list = "";

			for (Player player : players)
				_list += (_list == "" ? "" : ", ") + player.getName();

			dead = 0;
			points = 0;
		}

		public boolean isTeam()
		{
			return _players.size() > 1;
		}

		public boolean contains(Player p)
		{
			return _players.contains(p);
		}

		public String getName()
		{
			return _teamname;
		}

		public String getPlayers()
		{
			return _list;
		}

		public void addDead()
		{
			dead++;
		}

		public boolean hasLost()
		{
			return dead == _players.size();
		}

		public void resetDead()
		{
			dead = 0;
		}

		public void addPoint()
		{
			points++;
		}

		public int getPoints()
		{
			return points;
		}

		public void spawn(Location l)
		{
			for (Player p : _players)
			{

				if (p == null)
					continue;

				p.getInventory().clear();
				p.getInventory().setBoots(null);
				p.getInventory().setLeggings(null);
				p.getInventory().setChestplate(null);
				p.getInventory().setHelmet(null);

				/*
				 * for (PotionEffectType Po : PotionEffectType.values()) { if
				 * (p.hasPotionEffect(Po)) p.removePotionEffect(Po); }
				 */
				p.setHealth(20);
				p.setFoodLevel(30);
				p.setFireTicks(0);
				p.teleport(l);
			}
		}
	}
}
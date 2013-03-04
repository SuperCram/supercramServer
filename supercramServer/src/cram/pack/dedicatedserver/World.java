package cram.pack.dedicatedserver;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagMap;


public class World
{
	String name = "";
	String hash = "";
	public TagMap tag = null;
	WorldSprite[] clips = null;
	WorldSprite[] triggers = null;
	private int[] playerSpawns = null;
	private int[] mobSpawns = null;
	private int[] crateSpawnZones = null;
	private int gravity = 0;
	public int getGravity(){return gravity;}
	private int enemySpawnDelay = 0;
	private int lastEnemySpawn = 0;
	
	ArrayList<Player> players = new ArrayList<Player>();
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Enemy> crates = new ArrayList<Enemy>();
	ArrayList<Enemy> projectiles = new ArrayList<Enemy>();
	public World(File worldFile) throws IOException
	{
		DataInputStream dis = new DataInputStream(new FileInputStream(worldFile));
		tag = (TagMap)Tag.readTag(dis);
		dis.close();
		reloadFromTag();
	}
	private void reloadFromTag()
	{
		
	}
	void tick()
	{
		
	}
	public void dropCrate()
	{
		
	}
	public WorldSprite[] getClips() { return clips; }
	public Player[] getPlayers() { return (Player[])players.toArray(); }
	public void addProjectile(Projectile proj) {
		
	}
}
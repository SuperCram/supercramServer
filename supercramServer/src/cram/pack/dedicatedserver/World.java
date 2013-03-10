package cram.pack.dedicatedserver;

import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagBool;
import cram.pack.dedicatedserver.cereal.TagFloat;
import cram.pack.dedicatedserver.cereal.TagInt;
import cram.pack.dedicatedserver.cereal.TagMap;
import cram.pack.dedicatedserver.cereal.TagStaticList;
import cram.pack.dedicatedserver.cereal.TagString;


public class World
{
	String name = "";
	String hash = "";
	
	private WorldSprite[] triggers = null;
	
	Rectangle[] background = null;
	Rectangle[] foreground = null;
	Rectangle[] clips = null;
	
	private int[] playerSpawns = null;
	private int[] mobSpawns = null;
	private int[] crateSpawnZones = null;
	private float gravity = 0;
	public float getGravity(){return gravity;}
	private int enemySpawnDelay = 0;
	private int lastEnemySpawn = 0;
	
	ArrayList<Player> players = new ArrayList<Player>();
	ArrayList<Enemy> enemies = new ArrayList<Enemy>(20);
	ArrayList<Crate> crates = new ArrayList<Crate>(2);
	ArrayList<Projectile> projectiles = new ArrayList<Projectile>(20);
	File worldFile = null;
	SupercramServer serv;
	public World(SupercramServer s, File pWorldFile) throws IOException
	{
		serv = s;
		worldFile = pWorldFile;
		DataInputStream dis = new DataInputStream(new FileInputStream(worldFile));
		TagMap tag = (TagMap)Tag.readTag(dis);
		dis.close();
		
		
		Tag[] tags = ((TagStaticList)tag.get("playerSpawns")).get();
		playerSpawns = new int[tags.length*2];
		for(int i=0;i<tags.length;i++)
		{
			playerSpawns[(i*2)+0] = ((TagInt)((TagStaticList)tags[i]).get(0)).get();
			playerSpawns[(i*2)+1] = ((TagInt)((TagStaticList)tags[i]).get(1)).get();
		}
		tags = ((TagStaticList)tag.get("mobSpawns")).get();
		mobSpawns = new int[tags.length*2];
		for(int i=0;i<tags.length;i++)
		{
			mobSpawns[(i*2)+0] = ((TagInt)((TagStaticList)tags[i]).get(0)).get();
			mobSpawns[(i*2)+1] = ((TagInt)((TagStaticList)tags[i]).get(1)).get();
		}
		
		
		List<Rectangle> bg = new LinkedList<Rectangle>();
		List<Rectangle> fg = new LinkedList<Rectangle>();
		List<Rectangle> cp = new LinkedList<Rectangle>();
		gravity = ((TagFloat)tag.get("gravity")).get();
		for(Tag t : ((TagStaticList)tag.get("spirtes")).get())
		{
			TagMap spriteTag = (TagMap)t;
			TagStaticList AABB = (TagStaticList)spriteTag.get("aabb");
			
			Rectangle rec = new Rectangle(((TagInt)AABB.get(0)).get(), ((TagInt)AABB.get(1)).get(), ((TagInt)AABB.get(2)).get(), ((TagInt)AABB.get(3)).get());
			
			if(((TagBool)spriteTag.get("collisions")).get())
				cp.add(rec);
			if(((TagBool)spriteTag.get("background")).get())
				bg.add(rec);
			else
				fg.add(rec);
		}
		background = bg.toArray(new Rectangle[0]);
		foreground = fg.toArray(new Rectangle[0]);
		clips = cp.toArray(new Rectangle[0]);
		System.out.println("Clip count:"+clips.length);
		
		name = ((TagString)tag.get("name")).get();
		name = "demoworld";
		hash = ((TagString)tag.get("hash")).get();
		
		JFrame jf = new JFrame(name);
		//jf.setSize(800, 600);
		jf.add(worldDebug=new JPanelServerMap(this));
		jf.addKeyListener(worldDebug);
		jf.addMouseListener(worldDebug);
		jf.setVisible(true);
		
	}
	private JPanelServerMap worldDebug = null;
	int worldage = 0;
	void update()
	{
		if(worldDebug!=null)
			worldDebug.processKey();
		worldage++;
		//worldDebug.repaint();
		if(!players.isEmpty())
		{
			Iterator<Player> playerIterator = players.iterator();
			Player player = null;
			while(playerIterator.hasNext())
			{
				player = playerIterator.next();
				player.update();
				if(player.deleted)
					playerIterator.remove();
			}
		}
		if(!enemies.isEmpty())
		{
			Iterator<Enemy> enemyIterator = enemies.iterator();
			Entity enemy = null;
			while(enemyIterator.hasNext())
			{
				enemy = enemyIterator.next();
				enemy.update();
				if(enemy.deleted)
					enemyIterator.remove();
			}
		}
		if(!crates.isEmpty())
		{
			Iterator<Crate> playerIterator = crates.iterator();
			Entity player = null;
			while(playerIterator.hasNext())
			{
				player = playerIterator.next();
				player.update();
				if(player.deleted)
					playerIterator.remove();
			}
		}
		if(!projectiles.isEmpty())
		{
			Iterator<Projectile> playerIterator = projectiles.iterator();
			Entity player = null;
			while(playerIterator.hasNext())
			{
				player = playerIterator.next();
				player.update();
				if(player.deleted)
					playerIterator.remove();
			}
		}
	}
	private int UID = 0;
	public int[] getRandomPlayerSpawn()
	{
		if(playerSpawns.length!=0)
		{
			int indx = new Random().nextInt(playerSpawns.length/2);
			return new int[]{playerSpawns[indx*2],playerSpawns[(indx*2)+1]};
		}
		return new int[]{400,300};
	}
	public int[] getRandomCrateSpawn()
	{
		return new int[]{400,300};
	}
	public void addEntity(Entity ent)
	{
		ent.addedToWorld(this, UID);
		UID++;
		if(ent instanceof Player)
		{
			Player player = (Player)ent;
			player.nsh.entityTracker.clearEntities();
			for(Entity ent2 : getEntities())
				player.nsh.entityTracker.addEntity(ent2);
			players.add(player);
			
			System.out.println("PLAYER ADDED TO WORLD");
			return;
		}
		else if(ent instanceof Enemy)
			enemies.add((Enemy)ent);
		else if(ent instanceof Crate)
			crates.add((Crate)ent);
		else if(ent instanceof Projectile)
			projectiles.add((Projectile)ent);
		// Update player entity trackers
		if(!players.isEmpty())
		{
			Iterator<Player> playerIterator = players.iterator();
			Player player = null;
			while(playerIterator.hasNext())
			{
				player = playerIterator.next();
				if(player!=ent)
					player.nsh.entityTracker.addEntity(ent);
			}
		}
	}
	public void dropCrate()
	{
		System.out.println("Dropped crate");
		Crate crate = new Crate();
		crate.setXY(getRandomCrateSpawn());
		addEntity(crate);
		
	}
	public void dropEnemy()
	{
		System.out.println("Dropped enemy");
		Enemy enemy = new Enemy();
		enemy.setXY(getRandomEnemySpawn());
		addEntity(enemy);
	}
	private int[] getRandomEnemySpawn() {
		if(mobSpawns.length!=0)
		{
			int indx = new Random().nextInt(mobSpawns.length/2);
			return new int[]{mobSpawns[indx*2],mobSpawns[(indx*2)+1]};
		}
		return new int[]{200,100};
	}
	public LinkedList<Entity> getEntities()
	{
		LinkedList<Entity> ents = new LinkedList<Entity>();
		ents.addAll(players);
		ents.addAll(crates);
		ents.addAll(enemies);
		ents.addAll(projectiles);
		return ents;
	}
	public Rectangle[] getClips()			{ return clips; }
	public Player[] getPlayers()			{ return players.toArray(new Player[0]); }
	public Projectile[] getProjectiles()	{ return projectiles.toArray(new Projectile[0]); }
	public Enemy[] getEnemies()				{ return enemies.toArray(new Enemy[0]); }
}
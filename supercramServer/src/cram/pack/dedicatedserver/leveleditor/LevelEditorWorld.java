package cram.pack.dedicatedserver.leveleditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;

import cram.pack.dedicatedserver.WorldSprite;
import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagArrayList;
import cram.pack.dedicatedserver.cereal.TagMap;
import cram.pack.dedicatedserver.cereal.TagStaticList;

public class LevelEditorWorld extends JPanel implements MouseListener,MouseMotionListener,KeyListener
{
	private static final long serialVersionUID = 8672126115077137614L;
	String name = "Unnamed";
	String hash = "";
	
	ArrayList<LevelEditorPoint> playerSpawns = new ArrayList<LevelEditorPoint>();
	ArrayList<LevelEditorPoint> mobSpawns = new ArrayList<LevelEditorPoint>();
	ArrayList<LevelEditorZone> crateSpawnZones = new ArrayList<LevelEditorZone>();
	ArrayList<LevelEditorWorldSprite> sprites = new ArrayList<LevelEditorWorldSprite>();
	
	float gravity = 0;
	int enemySpawnDelay = 0;
	
	TagMap worldTagMap = null;
	File worldFile = null;
	LevelEditor mainFrame = null;
	public LevelEditorWorld(LevelEditor mainFrame)
	{
		this.mainFrame = mainFrame;
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(840,640));
		setSize(840, 640);
	}
	public LevelEditorWorld(LevelEditor mainFrame, File worldFile)
	{
		this(mainFrame);
		this.worldFile = worldFile;
		try
		{
			DataInputStream dis = new DataInputStream(new FileInputStream(worldFile));
			TagMap worldTagMap = TagMap.get(Tag.readTag(dis));
			dis.close();
			
			try { name = worldTagMap.getString("name"); } catch(Exception e) { name=""; }
			try { hash = worldTagMap.getString("hash"); } catch(Exception e) { hash=""; }
			try
			{
				for(Tag aSprite : worldTagMap.getStaticList("spirtes"))
				{
					LevelEditorWorldSprite ews = new LevelEditorWorldSprite(this,TagMap.get(aSprite));
					sprites.add(ews);
				}
			}
			catch(Exception e){}
			try
			{
				for(Tag aPlayerSpawn : worldTagMap.getStaticList("playerSpawns"))
				{
					TagStaticList playerSpawn = TagStaticList.getObject(aPlayerSpawn);
					playerSpawns.add(new LevelEditorPoint(true,playerSpawn.getInt(0),playerSpawn.getInt(1)));
				}
			}
			catch(Exception e){}
			try
			{
				for(Tag aMobSpawns : worldTagMap.getStaticList("mobSpawns"))
				{
					TagStaticList playerSpawn = TagStaticList.getObject(aMobSpawns);
					mobSpawns.add(new LevelEditorPoint(playerSpawn.getInt(0),playerSpawn.getInt(1)));
				}
			}
			catch(Exception e){}
			try
			{
				for(Tag aCrateSpawnZones : worldTagMap.getStaticList("crateSpawnZones"))
				{
					TagStaticList playerSpawn = TagStaticList.getObject(aCrateSpawnZones);
					crateSpawnZones.add(new LevelEditorZone(true,this,playerSpawn.getInt(0),playerSpawn.getInt(1),playerSpawn.getInt(2),playerSpawn.getInt(3)));
				}
			}catch(Exception e){}
			try { gravity = worldTagMap.getFloat("gravity"); } catch(Exception e) { }
			try { enemySpawnDelay = worldTagMap.getInt("enemySpawnDelay"); } catch(Exception e) { }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		recompileElements();
	}
	void recompileElements()
	{
		clickableElements = new LinkedList<LevelEditorSelectable>();
		/*
		 * backgrounds
		 * clips
		 * triggers
		 * crateSpawnZones
		 * playerSpawns
		 * mobSpawns
		 * foregrounds
		 */
		if(drawBackground)
			for(LevelEditorWorldSprite sprite : sprites)
				if(sprite.hasImage && sprite.background && !clickableElements.contains(sprite))
					clickableElements.add(sprite);
		if(drawClips)
			for(LevelEditorWorldSprite sprite : sprites)
				if(sprite.collisions && !clickableElements.contains(sprite))
					clickableElements.add(sprite);
		if(drawTriggers)
			for(LevelEditorWorldSprite sprite : sprites)
				if(sprite.trigger && !clickableElements.contains(sprite))
					clickableElements.add(sprite);
		if(drawTriggers)
			for(LevelEditorWorldSprite sprite : sprites)
				if(sprite.trigger && !clickableElements.contains(sprite))
					clickableElements.add(sprite);
		if(drawCrateSpawnZones)
		{
			clickableElements.addAll(crateSpawnZones);
		}
		if(drawPlayerSpawns)
			clickableElements.addAll(playerSpawns);
		if(drawMobSpawns)
			clickableElements.addAll(mobSpawns);
		if(drawForegrounds)
			for(LevelEditorWorldSprite sprite : sprites)
				if(sprite.hasImage && !sprite.background && !clickableElements.contains(sprite))
					clickableElements.add(sprite);
	}
	void updateSprite(LevelEditorWorldSprite el)
	{
		deleteElement(el);
		sprites.add(el);
		recompileElements();
	}
	void updatePlayerSpawn(LevelEditorPoint el)
	{
		deleteElement(el);
		playerSpawns.add(el);
		recompileElements();
	}
	void updateMobSpawn(LevelEditorPoint el)
	{
		deleteElement(el);
		mobSpawns.add(el);
		recompileElements();
	}
	void updateCrateSpawn(LevelEditorZone el)
	{
		deleteElement(el);
		crateSpawnZones.add(el);
		recompileElements();
	}
	public TagMap save()
	{
		TagMap tagMap = new TagMap();
		tagMap.setString("name", name);
		hash = "";
		for(int i=0;i<10;i++)
			hash += new Random().nextInt();
		tagMap.setString("hash", hash);
		
		TagArrayList tl = new TagArrayList();
		for(LevelEditorWorldSprite z : sprites)
			tl.add(z.toTag());
		tagMap.put("spirtes", tl);
		
		tl = new TagArrayList();
		for(LevelEditorPoint z : playerSpawns)
			tl.add(z.toTag());
		tagMap.put("playerSpawns", tl);
		
		tl = new TagArrayList();
		for(LevelEditorPoint z : mobSpawns)
			tl.add(z.toTag());
		tagMap.put("mobSpawns", tl);
		
		tl = new TagArrayList();
		for(LevelEditorZone z : crateSpawnZones)
			tl.add(z.toTag());
		tagMap.put("crateSpawnZones", tl);
		
		tagMap.setFloat("gravity", gravity);
		tagMap.setInt("enemySpawnDelay", enemySpawnDelay);
		return tagMap;
	}
	LinkedList<LevelEditorSelectable> clickableElements = new LinkedList<LevelEditorSelectable>();
	public boolean render = false;
	public boolean drawBackground = true;
	public boolean drawClips = true;
	public boolean drawTriggers = true;
	public boolean drawCrateSpawnZones = true;
	public boolean drawPlayerSpawns = true;
	public boolean drawMobSpawns = true;
	public boolean drawForegrounds = false;
	
	public boolean graphicsMode = false;
	
	int resolution = 1;
	boolean bounds = true;
	boolean grid = true;
	
	public void draw(Graphics g) {
		g.clearRect(0, 0, 840, 640);
		g.setColor(Color.BLACK);
		g.drawRect(20, 20, 800, 600);
		for(LevelEditorSelectable ews : clickableElements)
			if(ews!=selection)
				ews.draw(g);
		if(selection!=null)
			selection.draw(g);
	}
	@Override
	public void mouseClicked(MouseEvent paramMouseEvent) {
		if(paramMouseEvent.getClickCount()==2 && selection!=null)
			selection.doubleClick(0, 0);
	}
	@Override
	public void mousePressed(MouseEvent paramMouseEvent) {
		selectElement(paramMouseEvent.getX()-LevelEditor.offsetX,paramMouseEvent.getY()-LevelEditor.offsetY);
	}
	@Override
	public void mouseReleased(MouseEvent paramMouseEvent) {
		if(selection!=null)
			selection.mouseUp();
	}
	@Override
	public void mouseEntered(MouseEvent paramMouseEvent) {
		
	}
	@Override
	public void mouseExited(MouseEvent paramMouseEvent) {
		
	}
	@Override
	public void mouseDragged(MouseEvent paramMouseEvent) {
		if(selection!=null)
			selection.mouseMove(paramMouseEvent.getX()-LevelEditor.offsetX, resolutionGrid(paramMouseEvent.getY()-LevelEditor.offsetY));
	}
	@Override
	public void mouseMoved(MouseEvent paramMouseEvent) {
		int activeCursor = Cursor.DEFAULT_CURSOR;
		for(LevelEditorSelectable w : clickableElements)
		{
			int temp = w.updateCursor(mainFrame,paramMouseEvent.getX()-LevelEditor.offsetX,paramMouseEvent.getY()-LevelEditor.offsetY);
			if(temp!=-1)
				activeCursor = temp;
		}
		mainFrame.setCursor(new Cursor(activeCursor));
	}
	private void selectElement(int x, int y)
	{
		if(selection!=null && selection.shouldSelect(x, y))
		{
			selection.setSelected(true, x, y);
			return;
		}
		LevelEditorSelectable newSelected = null;
		for(LevelEditorSelectable sel : clickableElements)
			if(sel.shouldSelect(x, y))
				newSelected = sel;
		if(newSelected!=selection && selection!=null)
			selection.setSelected(false,0,0);
		selection = newSelected;
		if(selection!=null)
			selection.setSelected(true,x,y);
	}
	LevelEditorSelectable selection = null;
	public void deleteElement(LevelEditorSelectable el)
	{
		Iterator<LevelEditorPoint> it = null;
		if(!playerSpawns.isEmpty())
		{
			it = playerSpawns.iterator();
			while(it.hasNext())
				if(it.next()==el)
					it.remove();
		}
		if(!mobSpawns.isEmpty())
		{
			it = mobSpawns.iterator();
			while(it.hasNext())
				if(it.next()==el)
					it.remove();
		}
		
		if(!crateSpawnZones.isEmpty())
		{
			Iterator<LevelEditorZone> it2 = crateSpawnZones.iterator();
			while(it2.hasNext())
				if(it2.next()==el)
					it2.remove();
		}
		if(!sprites.isEmpty())
		{
			Iterator<LevelEditorWorldSprite> it3 = sprites.iterator();
			while(it3.hasNext())
				if(it3.next()==el)
					it3.remove();
		}
		recompileElements();
	}
	@Override
	public void keyTyped(KeyEvent paramKeyEvent) {
		
	}
	@Override
	public void keyPressed(KeyEvent paramKeyEvent) {
		
	}
	@Override
	public void keyReleased(KeyEvent paramKeyEvent) {
		if(paramKeyEvent.getKeyCode()==KeyEvent.VK_B)
		{
			setBounds(!bounds);
		}
		else if(paramKeyEvent.getKeyCode()==KeyEvent.VK_SPACE)
		{
			if(selection instanceof LevelEditorWorldSprite)
			{
				LevelEditorWorldSprite s = (LevelEditorWorldSprite)selection;
				updateSprite(s.duplicate());
			}
		}
		else if(paramKeyEvent.getKeyCode()==KeyEvent.VK_DELETE || paramKeyEvent.getKeyCode()==KeyEvent.VK_BACK_SPACE)
		{
			if(selection!=null)
			{
				deleteElement(selection);
				selection.setSelected(false, 0, 0);
				selection = null;
				return;
			}
			
		}
		else
		{
			switch(paramKeyEvent.getKeyChar())
			{
				case '1': setResolution(1); return;
				case '2': setResolution(2); return;
				case '3': setResolution(4); return;
				case '4': setResolution(8); return;
				case '5': setResolution(16); return;
				case '6': setResolution(32); return;
				case '7': setResolution(64); return;
				case '8': setResolution(128); return;
				case '9': setResolution(256); return;
			}
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 600);
		draw(g);
		try {
			Thread.sleep(16);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.repaint();
	}
	public int resolutionGrid(int i)
	{
		if(resolution==1)
			return i;
		return Math.round(i/resolution) * resolution;
	}
	public void setResolution(int i)
	{
		resolution = i;
		mainFrame.updateControls();
	}
	public void select(LevelEditorSelectable newSprite) {
		if(selection!=null)
			selection.setSelected(false, 0, 0);
		selection = newSprite;
		selection.setSelected(true, 0, 0);
		selection.mouseUp();
	}
	public void setBounds(boolean b) {
		bounds = b;
		mainFrame.updateControls();
	}
}

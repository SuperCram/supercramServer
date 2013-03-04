package cram.pack.dedicatedserver;

import java.util.ArrayList;
import java.util.Iterator;

import cram.pack.dedicatedserver.cereal.TagMap;


public class World
{
	String name = "";
	String hash = "";
	public TagMap tag = null;
	
	ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	ArrayList<Player> players = new ArrayList<Player>();
	ArrayList<WorldSprite> clips = new ArrayList<WorldSprite>();
	float gravity = 2f;
	
	
	public void tick()
	{
		
	}
	public void toTag()
	{
		
	}
	public Iterator<WorldSprite> clipIterator()
	{
		return clips.iterator();
	}
}
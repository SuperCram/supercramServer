package cram.pack.dedicatedserver.leveleditor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;

import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagImage;
import cram.pack.dedicatedserver.cereal.TagInt;
import cram.pack.dedicatedserver.cereal.TagMap;
import cram.pack.dedicatedserver.cereal.TagStaticList;
import cram.pack.dedicatedserver.cereal.TagString;

public class LevelEditorWorldSprite extends LevelEditorZone implements LevelEditorSelectable
{
	boolean trigger = false;
	boolean collisions = true;
	boolean hasImage = true;
	boolean background = true;
	
	boolean hasRGBA = false;
	Image img;
	byte red,green,blue,alpha;
	HashMap<String,String> params = new HashMap<String,String>();
	public LevelEditorWorldSprite(LevelEditorWorld wld) {
		super(wld,10,10,100,100);
	}
	public LevelEditorWorldSprite(LevelEditorWorld wld, TagMap tagMap) {
		super(wld);
		TagStaticList tsl = (TagStaticList)tagMap.get("aabb");
		aabb = new Rectangle(tsl.getInt(0),tsl.getInt(1),tsl.getInt(2),tsl.getInt(3));
		selectionField = aabb;
		collisions = tagMap.getBool("collisions");
		//hasImage = tagMap.getBool("hasImage");
		Tag imageTag = tagMap.get("image");
		if(imageTag instanceof TagImage)
			img = ((TagImage)imageTag).get();
		else
		{
			hasRGBA = true;
			int rgba = ((TagInt)imageTag).get();
			red = (byte) ((rgba<<24)&0xFF);
			green = (byte) ((rgba<<16)&0xFF);
			blue = (byte) ((rgba<<8)&0xFF);
			alpha = (byte) (rgba&0xFF);
		}
		if(hasImage)
			background = tagMap.getBool("background");
		trigger = tagMap.getBool("trigger");
		TagMap tagMapParams = tagMap.getMap("params");
		for(Entry<String,Tag> tags : tagMapParams.entrySet())
			params.put(tags.getKey(), TagString.get(tags.getValue()));
		
	}
	
	@Override
	public Tag toTag() {
		TagMap tagMap = new TagMap();
		TagMap tagParams = new TagMap();
		for(Entry<String,String> maps : params.entrySet())
			tagParams.setString(maps.getKey(), maps.getValue());
		tagMap.put("aabb", super.toTag());
		tagMap.setBool("collisions", collisions);
		tagMap.setBool("hasImage", hasImage);
		if(hasImage)
			tagMap.setBool("background", background);
		tagMap.setBool("trigger", trigger);
		tagMap.put("params", tagParams);
		return tagMap;
	}
	
	public LevelEditorWorldSprite duplicate()
	{
		LevelEditorWorldSprite w = new LevelEditorWorldSprite(world);
		w.aabb = aabb.getBounds();
		w.selectionField = w.aabb;
		
		w.trigger = trigger;
		w.collisions = collisions;
		w.hasImage = hasImage;
		w.background = background;
		for(Entry<String,String> tags : params.entrySet())
			w.params.put(tags.getKey(), tags.getValue());
		return w;
	}
	
	@Override
	public void doubleClick(int x, int y) {
		new LevelEditorSpriteEditor(this);
	}
	@Override
	public void draw(Graphics g) {
		if(world.render && hasImage)
		{
			if(hasRGBA)
				g.setColor(new Color(red,green,blue,alpha));
			else
			{
				g.drawImage(img,aabb.x,aabb.y,aabb.width,aabb.height,null);
				g.setColor(Color.BLACK);
				g.drawRect(aabb.x+LevelEditor.offsetX,aabb.y+LevelEditor.offsetY,aabb.width,aabb.height);
				if(selected)
				{
					g.setColor(new Color(148,0,211));
					g.drawRect(selectionField.x+LevelEditor.offsetX, selectionField.y+LevelEditor.offsetY, selectionField.width, selectionField.height);
				}
				return;
			}
		}
		else
		{
			if(world.drawBackground && hasImage && background)
				g.setColor(Color.DARK_GRAY);
			if(world.drawBackground && hasImage && !background)
				g.setColor(Color.LIGHT_GRAY);
			
			if(world.drawClips && collisions)
				g.setColor(Color.green);
			if(world.drawTriggers && trigger)
				g.setColor(Color.magenta);
		}
		super.draw(g);
	}
}

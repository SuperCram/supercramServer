package cram.pack.dedicatedserver.leveleditor;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map.Entry;

import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagMap;
import cram.pack.dedicatedserver.cereal.TagStaticList;
import cram.pack.dedicatedserver.cereal.TagString;

public class LevelEditorWorldSprite extends LevelEditorZone implements LevelEditorSelectable
{
	boolean trigger = false;
	boolean collisions = true;
	boolean hasImage = true;
	boolean background = true;
	HashMap<String,String> params = new HashMap<String,String>();
	public LevelEditorWorldSprite(LevelEditorWorld wld) {
		super(wld,10,10,10,10);
	}
	public LevelEditorWorldSprite(LevelEditorWorld wld, TagMap tagMap) {
		super(wld);
		TagStaticList tsl = (TagStaticList)tagMap.get("aabb");
		aabb = new Rectangle(tsl.getInt(0),tsl.getInt(1),tsl.getInt(2),tsl.getInt(3));
		selectionField = aabb;
		collisions = tagMap.getBool("collisions");
		//hasImage = tagMap.getBool("hasImage");
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
}

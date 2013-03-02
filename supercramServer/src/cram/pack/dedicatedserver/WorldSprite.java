package cram.pack.dedicatedserver;

import java.util.HashMap;

import cram.pack.dedicatedserver.ser.TagBool;
import cram.pack.dedicatedserver.ser.TagInt;
import cram.pack.dedicatedserver.ser.TagMap;
import cram.pack.dedicatedserver.ser.TagStaticList;

public class WorldSprite
{
	int x = 0;
	int y = 0;
	int width = 0;
	int height = 0;
	boolean collide = false;
	boolean trigger = false;
	HashMap<String,String> params = new HashMap<String,String>();
	public static WorldSprite createWorldSprite(TagMap sprite)
	{
		WorldSprite ws = new WorldSprite();
		TagStaticList AABB = ((TagStaticList)sprite.get("aabb"));
		ws.y = ((TagInt)AABB.get(0)).get();
		ws.x = ((TagInt)AABB.get(1)).get();
		ws.width = ((TagInt)AABB.get(2)).get();
		ws.height = ((TagInt)AABB.get(3)).get();
		
		ws.collide = ((TagBool)sprite.get("collisions")).get();
		ws.trigger = ((TagBool)sprite.get("trigger")).get();
		
		((TagMap)sprite.get("trigger")).entrySet();
		return ws;
		/*rect = spriteDict["aabb"].data
	    spr.rect = pygame.Rect(rect[0].data, rect[1].data, rect[2].data, rect[3].data)
	    spr.collisions = spriteDict["collisions"].data
	    spr.background = spriteDict["background"].data
	    spr.trigger = spriteDict["trigger"].data
	    paramMap = spriteDict["params"].data*/
	}
}

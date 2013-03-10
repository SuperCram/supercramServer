package cram.pack.dedicatedserver.leveleditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;

import javax.swing.JFrame;

import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagInt;
import cram.pack.dedicatedserver.cereal.TagStaticList;

public class LevelEditorPoint implements LevelEditorSelectable {
	int x=0,y=0;
	public LevelEditorPoint(int pX, int pY)
	{
		x = pX;
		y = pY;
	}
	public void draw(Graphics g)
	{
		g.drawLine(x-10+LevelEditor.offsetX, y+LevelEditor.offsetY, x+10+LevelEditor.offsetX, y+LevelEditor.offsetY);
		g.drawLine(x+LevelEditor.offsetX, y-10+LevelEditor.offsetY, x+LevelEditor.offsetX, y+10+LevelEditor.offsetY);
		if(selected)
		{
			Color c = g.getColor();
			g.setColor(new Color(148,0,211));
			g.drawLine(x-7+LevelEditor.offsetX, y-7+LevelEditor.offsetY, x+7+LevelEditor.offsetX, y+7+LevelEditor.offsetY);
			g.drawLine(x+7+LevelEditor.offsetX, y-7+LevelEditor.offsetY, x-7+LevelEditor.offsetX, y+7+LevelEditor.offsetY);
			g.setColor(c);
		}
	}
	
	@Override
	public boolean shouldSelect(int px, int py) {
		return ((px-x)*(px-x))+((py-y)*(py-y))<100;
	}
	boolean selected = false;
	boolean dragging = false;
	@Override
	public void setSelected(boolean flag,int x, int y) {
		selected = flag;
		if(selected)
			dragging = true;
	}
	@Override
	public boolean isSelected(boolean flag) {
		return selected;
	}
	@Override
	public void mouseUp() {
		dragging = false;
	}
	@Override
	public void mouseMove(int x, int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public int updateCursor(JFrame mainFrame, int x, int y) {
		if(shouldSelect(x,y))
			return Cursor.CROSSHAIR_CURSOR;
		else
			return -1;
	}
	public Tag toTag()
	{
		return new TagStaticList(new Tag[]{new TagInt(x),new TagInt(y)});
	}
}

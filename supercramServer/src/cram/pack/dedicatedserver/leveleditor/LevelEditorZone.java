package cram.pack.dedicatedserver.leveleditor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;

import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagInt;
import cram.pack.dedicatedserver.cereal.TagStaticList;

public class LevelEditorZone implements LevelEditorSelectable
{
	public LevelEditorZone(LevelEditorWorld wld)
	{
		world = wld;
	}
	boolean isCrateZone = false;
	LevelEditorWorld world = null;
	public LevelEditorZone(boolean b, LevelEditorWorld wld, int x, int y, int w, int h)
	{
		this(wld,x,y,w,h);
		isCrateZone = b;
	}
	public LevelEditorZone(LevelEditorWorld wld, int x, int y, int w, int h)
	{
		this(wld);
		aabb = new Rectangle(x,y,w,h);
		selectionField = aabb;
	}
	Rectangle aabb = null;
	Rectangle selectionField = null;
	boolean selected = false;
	boolean dragMoving = false;
	@Override
	public boolean shouldSelect(int x, int y) {
		return selectionField.contains(x,y);
	}
	@Override
	public boolean isSelected(boolean flag) {
		return selected;
	}
	public void updateSelectionField()
	{
		selectionField = new Rectangle(aabb.x-10,aabb.y-10,aabb.width+20,aabb.height+20);
	}
	@Override
	public void setSelected(boolean flag, int x, int y) {
		if(!flag)
			selectionField = aabb;
		else
		{
			startMouseX = x;
			startMouseY = y;
			startX = aabb.x;
			startY = aabb.y;
			startW = aabb.width;
			startH = aabb.height;
			
			updateSelectionField();
			if(aabb.contains(x, y))
			{
				dragMoving = true;
			}
			else if(selected)
			{
				if(x<(selectionField.x+selectionField.width) && x>(aabb.x+aabb.width))
					changingRight = true;
				if(y<(selectionField.y+selectionField.height) && y>(aabb.y+aabb.height))
					changingBottom = true;
				if(x>selectionField.x && x<aabb.x)
					changingLeft = true;
				if(y>selectionField.y && y<aabb.y)
					changingTop = true;
			}
			else
			{
				dragMoving = true;
				changingTop = false;
				changingLeft = false;
				changingRight = false;
				changingBottom = false;
			}
		}
		selected = flag;
	}
	boolean changingTop,changingLeft,changingRight,changingBottom;
	int startMouseX,startMouseY,startX,startY,startW,startH;
	@Override
	public void mouseUp() {
		dragMoving = false;
		changingTop = false;
		changingLeft = false;
		changingRight = false;
		changingBottom = false;
	}
	@Override
	public void mouseMove(int x, int y) {
		if(changingTop)
		{
			aabb.y = world.resolutionGrid(startY+(y-startMouseY));
			if(world.bounds && aabb.y<=0)
				aabb.y = 0;
			if(startH+(startY-aabb.y)<=0)
				aabb.y = startY+startH;
			aabb.height = Math.max(0,startH+(startY-aabb.y));
		}
		if(changingLeft)
		{
			aabb.x = world.resolutionGrid(startX+(x-startMouseX));
			if(world.bounds && aabb.x<=0)
				aabb.x = 0;
			if(startW+(startX-aabb.x)<=0)
				aabb.x = startX+startW;
			aabb.width = Math.max(0,startW+(startX-aabb.x));
		}
		if(changingBottom)
		{
			aabb.height = Math.max(0, world.resolutionGrid(startH+(y-startMouseY)));
			if(world.bounds && (aabb.y+aabb.height)>=600)
				aabb.height = 600-aabb.y;
		}
		if(changingRight)
		{
			aabb.width = Math.max(0, world.resolutionGrid(startW+(x-startMouseX)));
			if(world.bounds && (aabb.x+aabb.width)>=800)
				aabb.width = 800-aabb.x;
		}
		if(dragMoving)
		{
			aabb.x = world.resolutionGrid(startX+(x-startMouseX));
			aabb.y = world.resolutionGrid(startY+(y-startMouseY));
			if(world.bounds && aabb.x<=0)
				aabb.x = 0;
			if(world.bounds && aabb.y<=0)
				aabb.y = 0;
			if(world.bounds && (aabb.x+aabb.width)>=800)
				aabb.x = 800-aabb.width;
			if(world.bounds && (aabb.y+aabb.height)>=600)
				aabb.y = 600-aabb.height;
				
		}
		selectionField = new Rectangle(aabb.x-10,aabb.y-10,aabb.width+20,aabb.height+20);
	}
	public void draw(Graphics g)
	{
		if(isCrateZone)
			g.setColor(Color.orange);
		g.fillRect(aabb.x+LevelEditor.offsetX,aabb.y+LevelEditor.offsetY,aabb.width,aabb.height);
		g.setColor(Color.BLACK);
		g.drawRect(aabb.x+LevelEditor.offsetX,aabb.y+LevelEditor.offsetY,aabb.width,aabb.height);
		/*
		if(selected)
		{
			
			g.setColor(new Color(216,191,216));
			g.fillRect(aabb.x+LevelEditor.offsetX,aabb.y+LevelEditor.offsetY,aabb.width,aabb.height);
			g.setColor(Color.BLACK);
			g.drawRect(aabb.x+LevelEditor.offsetX,aabb.y+LevelEditor.offsetY,aabb.width,aabb.height);
		}
		else
		{
			
		}*/
		if(selected)
		{
			g.setColor(new Color(148,0,211));
			g.drawRect(selectionField.x+LevelEditor.offsetX, selectionField.y+LevelEditor.offsetY, selectionField.width, selectionField.height);
		}
	}
	@Override
	public int updateCursor(JFrame mainFrame, int x, int y) {
		if(shouldSelect(x, y))
		{
			int i = Cursor.MOVE_CURSOR;
			if(selected)
			{
				boolean N = (y>selectionField.y && y<aabb.y);
				boolean E = (x<(selectionField.x+selectionField.width) && x>(aabb.x+aabb.width));
				boolean S = (y<(selectionField.y+selectionField.height) && y>(aabb.y+aabb.height));
				boolean W = (x>selectionField.x && x<aabb.x);
				if(N && E)
					i = Cursor.NE_RESIZE_CURSOR;
				else if(N && W)
					i = Cursor.NW_RESIZE_CURSOR;
				else if(S && E)
					i = Cursor.SE_RESIZE_CURSOR;
				else if(S && W)
					i = Cursor.SW_RESIZE_CURSOR;
				else if(N)
					i = Cursor.N_RESIZE_CURSOR;
				else if(E)
					i = Cursor.E_RESIZE_CURSOR;
				else if(S)
					i = Cursor.S_RESIZE_CURSOR;
				else if(W)
					i = Cursor.W_RESIZE_CURSOR;
			}
			return i;
		}
		return -1;
	}
	public Tag toTag()
	{
		return new TagStaticList(new Tag[]{new TagInt(aabb.x),new TagInt(aabb.y),new TagInt(aabb.width),new TagInt(aabb.height)});
	}
	@Override
	public void doubleClick(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
}

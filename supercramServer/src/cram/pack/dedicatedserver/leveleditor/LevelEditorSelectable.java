package cram.pack.dedicatedserver.leveleditor;

import java.awt.Graphics;

import javax.swing.JFrame;

public interface LevelEditorSelectable {
	public boolean shouldSelect(int x, int y);
	public void setSelected(boolean flag,int x,int y);
	public boolean isSelected(boolean flag);
	public void mouseUp();
	public void mouseMove(int x,int y);
	public int updateCursor(JFrame mainFrame, int x, int y);
	public void draw(Graphics g);
}


package cram.pack.dedicatedserver.leveleditor;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class LevelEditor extends JFrame
{
	public static final int offsetX = 20;
	public static final int offsetY = 20;
	private static final long serialVersionUID = 7651966161866263188L;
	public LevelEditor()
	{
		super("Level Editor");
		
		LevelEditorMenuListener menuListener = new LevelEditorMenuListener(this);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem tempJMenuItem = null;
			tempJMenuItem = new JMenuItem("New",KeyEvent.VK_N);
			tempJMenuItem.setActionCommand("new");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			tempJMenuItem = new JMenuItem("Open File",KeyEvent.VK_O);
			tempJMenuItem.setActionCommand("openFile");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			menu.addSeparator();
			
			tempJMenuItem = new JMenuItem("Save",KeyEvent.VK_S);
			tempJMenuItem.setActionCommand("save");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			tempJMenuItem = new JMenuItem("Save As...",KeyEvent.VK_O);
			tempJMenuItem.setActionCommand("saveAs");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			menu.addSeparator();
			
			tempJMenuItem = new JMenuItem("Reload",KeyEvent.VK_O);
			tempJMenuItem.setActionCommand("reload");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
		menuBar.add(menu);
		menu = new JMenu("Add");
			tempJMenuItem = new JMenuItem("Sprite");
			tempJMenuItem.setActionCommand("addSprite");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			tempJMenuItem = new JMenuItem("Player Spawn Point");
			tempJMenuItem.setActionCommand("addPlayerSpawnPoint");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			tempJMenuItem = new JMenuItem("Enemy Spawn Point");
			tempJMenuItem.setActionCommand("addEnemySpawnPoint");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
			
			tempJMenuItem = new JMenuItem("Crate Spawn Zone");
			tempJMenuItem.setActionCommand("addCrateSpawnZone");
			tempJMenuItem.addActionListener(menuListener);
			menu.add(tempJMenuItem);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		updateWindow(new LevelEditorWorld(this));
		setResizable(false);
		pack();
		setVisible(true);
	}
	
	LevelEditorWorld levelEditorWorld = null;
	
	private File activeWorldFile = null;
	
	public void newWorld() {
		activeWorldFile = null;
		updateWindow(new LevelEditorWorld(this));
	}
	public void openWorld(File file) throws IOException {
		if(file!=null)
			activeWorldFile = file;
		updateWindow(new LevelEditorWorld(this,activeWorldFile));
	}
	public void saveWorld(File file) throws IOException {
		if(file!=null)
			activeWorldFile = file;
		if(activeWorldFile!=null)
		{
			DataOutputStream dis = new DataOutputStream(new FileOutputStream(activeWorldFile));
			levelEditorWorld.save().writeAll(dis);
			dis.close();
		}
	}
	public void updateWindow(LevelEditorWorld lew)
	{
		if(levelEditorWorld!=null)
		{
			remove(levelEditorWorld);
			removeKeyListener(levelEditorWorld);
		}
		levelEditorWorld = lew;
		add(levelEditorWorld);
		addKeyListener(levelEditorWorld);
		repaint();
	}
	public static void main(String args[])
	{
		if(args.length>1)
		{
			System.out.println("javaw -cp SupercramServer.jar cram.pack.dedicatedserver.leveleditor.LevelEditor <some server>/worlds/<some world>");
			return;
		}
		LevelEditor le = new LevelEditor();
		if(args.length==0)
		{
			le.newWorld();
			return;
		}
		else
		{
			File f = new File(".",args[0]);
			if(!f.exists())
				System.out.println("Specified file doesn't exist!");
			if(f.isDirectory())
				System.out.println("Specified file is a directory!");
			try {
				le.openWorld(f);
			} catch (IOException e) {
				System.out.println("Failed to open file: "+e.getMessage());
			}
		}
	}
}

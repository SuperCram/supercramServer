package cram.pack.dedicatedserver.leveleditor;

import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

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
			menu.add(prime("New","new", menuListener));
			menu.add(prime("Open","openFile", menuListener));
			menu.add(prime("Save","save", menuListener));
			menu.add(prime("Save as...","saveAs", menuListener));
			menu.add(prime("Reload","reload", menuListener));
		menuBar.add(menu);
		menu = new JMenu("Edit");
			menu.add(prime("Delete","deleteSelection",menuListener));
			menu.add(prime("Duplicate","duplicateSelection",menuListener));
		menuBar.add(menu);
		menu = new JMenu("Grid");
			menu.add(resolutionControls[0]=createJRadioButtonMenuItem("1px Resolution","setResolution/1",menuListener,true));
			menu.add(resolutionControls[1]=createJRadioButtonMenuItem("2px Resolution","setResolution/2",menuListener,false));
			menu.add(resolutionControls[2]=createJRadioButtonMenuItem("4px Resolution","setResolution/4",menuListener,false));
			menu.add(resolutionControls[3]=createJRadioButtonMenuItem("8px Resolution","setResolution/8",menuListener,false));
			menu.add(resolutionControls[4]=createJRadioButtonMenuItem("16px Resolution","setResolution/16",menuListener,false));
			menu.add(resolutionControls[5]=createJRadioButtonMenuItem("32px Resolution","setResolution/32",menuListener,false));
			menu.add(resolutionControls[6]=createJRadioButtonMenuItem("64px Resolution","setResolution/64",menuListener,false));
			menu.add(resolutionControls[7]=createJRadioButtonMenuItem("128px Resolution","setResolution/128",menuListener,false));
			menu.add(resolutionControls[8]=createJRadioButtonMenuItem("256px Resolution","setResolution/256",menuListener,false));
			menu.addSeparator();
			menu.add(boundsJCheckBoxMenuItem=createJCheckBoxMenuItem("Bounds","setBounds",menuListener,true));
			menu.add(gridJCheckBoxMenuItem=createJCheckBoxMenuItem("Grid Displayed","setGrid",menuListener,false));
		menuBar.add(menu);
		menu = new JMenu("Add");
			menu.add(prime("Sprite","addSprite", menuListener));
			menu.add(prime("Player Spawn Point","addPlayerSpawnPoint", menuListener));
			menu.add(prime("Enemy Spawn Point","addEnemySpawnPoint", menuListener));
			menu.add(prime("Crate Spawn Zone","addCrateSpawnZone", menuListener));
		menuBar.add(menu);
		menu = new JMenu("View");
			menu.add(renderCheckbox=createJCheckBoxMenuItem("Render","updateView",menuListener,false));
			menu.addSeparator();
			menu.add(backgroundCheckbox=createJCheckBoxMenuItem("Backgrounds","updateView",menuListener,true));
			menu.add(clipCheckbox=createJCheckBoxMenuItem("Clips","updateView",menuListener,true));
			menu.add(triggerCheckbox=createJCheckBoxMenuItem("Triggers","updateView",menuListener,true));
			menu.add(crateSpawnZoneCheckbox=createJCheckBoxMenuItem("Crate Spawn Zones","updateView",menuListener,true));
			menu.add(playerSpawnsCheckbox=createJCheckBoxMenuItem("Player Spawns","updateView",menuListener,true));
			menu.add(mobSpawnCheckbox=createJCheckBoxMenuItem("Mob Spawns","updateView",menuListener,true));
			menu.add(foregroundCheckbox=createJCheckBoxMenuItem("Foregrounds","updateView",menuListener,false));
			menu.addSeparator();
			menu.add(prime("Entity View","updateView/entity", menuListener));
			menu.add(prime("Graphics View","updateView/graphics", menuListener));
		menuBar.add(menu);
		setJMenuBar(menuBar);
		updateWindow(new LevelEditorWorld(this));
		setResizable(false);
		pack();
		setVisible(true);
	}
	private JCheckBoxMenuItem createJCheckBoxMenuItem(String string, String string2, LevelEditorMenuListener menuListener, boolean b) {
		JCheckBoxMenuItem ite = new JCheckBoxMenuItem(string);
		ite.setActionCommand(string2);
		ite.addActionListener(menuListener);
		ite.setSelected(b);
		return ite;
	}
	private JRadioButtonMenuItem createJRadioButtonMenuItem(String string, String string2, LevelEditorMenuListener menuListener, boolean b) {
		JRadioButtonMenuItem ite = new JRadioButtonMenuItem(string);
		ite.setActionCommand(string2);
		ite.addActionListener(menuListener);
		ite.setSelected(b);
		return ite;
	}
	private JMenuItem prime(String name, String action, ActionListener Listener)
	{
		JMenuItem item = new JMenuItem(name);
		item.setActionCommand(action);
		item.addActionListener(Listener);
		return item;
	}
	void updateDrawingControls()
	{
		levelEditorWorld.render = renderCheckbox.isSelected();
		levelEditorWorld.drawBackground = backgroundCheckbox.isSelected();
		levelEditorWorld.drawClips = clipCheckbox.isSelected();
		levelEditorWorld.drawTriggers = triggerCheckbox.isSelected();
		levelEditorWorld.drawCrateSpawnZones = crateSpawnZoneCheckbox.isSelected();
		levelEditorWorld.drawPlayerSpawns = playerSpawnsCheckbox.isSelected();
		levelEditorWorld.drawMobSpawns = mobSpawnCheckbox.isSelected();
		levelEditorWorld.drawForegrounds = foregroundCheckbox.isSelected();
		levelEditorWorld.recompileElements();
	}
	void updateControls()
	{
		boundsJCheckBoxMenuItem.setSelected(levelEditorWorld.bounds);
		gridJCheckBoxMenuItem.setSelected(levelEditorWorld.grid);
		for(int i=0;i<9;i++)
			resolutionControls[i].setSelected(false);
		switch(levelEditorWorld.resolution)
		{
		case 1: resolutionControls[0].setSelected(true); return;
		case 2: resolutionControls[1].setSelected(true); return;
		case 4: resolutionControls[2].setSelected(true); return;
		case 8: resolutionControls[3].setSelected(true); return;
		case 16: resolutionControls[4].setSelected(true); return;
		case 32: resolutionControls[5].setSelected(true); return;
		case 64: resolutionControls[6].setSelected(true); return;
		case 128: resolutionControls[7].setSelected(true); return;
		case 256: resolutionControls[8].setSelected(true); return;
		}
	}
	JCheckBoxMenuItem boundsJCheckBoxMenuItem = null;
	JCheckBoxMenuItem gridJCheckBoxMenuItem = null;
	
	JRadioButtonMenuItem[] resolutionControls = new JRadioButtonMenuItem[9];
	
	JCheckBoxMenuItem renderCheckbox = null;
	
	JCheckBoxMenuItem backgroundCheckbox = null;
	JCheckBoxMenuItem clipCheckbox = null;
	JCheckBoxMenuItem triggerCheckbox = null;
	JCheckBoxMenuItem crateSpawnZoneCheckbox = null;
	JCheckBoxMenuItem playerSpawnsCheckbox = null;
	JCheckBoxMenuItem mobSpawnCheckbox = null;
	JCheckBoxMenuItem foregroundCheckbox = null;
	
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

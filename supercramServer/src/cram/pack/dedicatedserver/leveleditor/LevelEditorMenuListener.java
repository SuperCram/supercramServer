package cram.pack.dedicatedserver.leveleditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class LevelEditorMenuListener implements ActionListener
{
	LevelEditor levelEditor = null;
	public LevelEditorMenuListener(LevelEditor ed)
	{
		levelEditor = ed;
	}
	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		if("new".equals(paramActionEvent.getActionCommand()))
		{
			levelEditor.newWorld();
		}
		else if("openFile".equals(paramActionEvent.getActionCommand()))
		{
			JFileChooser s = new JFileChooser(new File("."));
			int ret = s.showOpenDialog(levelEditor);
			if(ret==JFileChooser.APPROVE_OPTION)
			{
				System.out.println("OPENING");
				File file = s.getSelectedFile();
				try { levelEditor.openWorld(file); } catch(Exception e) { e.printStackTrace(); }
			}
		}
		else if("save".equals(paramActionEvent.getActionCommand()))
		{
			try { levelEditor.saveWorld(null); } catch(Exception e) { }
		}
		else if("saveAs".equals(paramActionEvent.getActionCommand()))
		{
			JFileChooser s = new JFileChooser();
			int ret = s.showSaveDialog(levelEditor);
			if(ret==JFileChooser.APPROVE_OPTION)
			{
				File file = s.getSelectedFile();
				try { levelEditor.saveWorld(file); } catch(Exception e) { e.printStackTrace(); }
			}
		}
		else if("reload".equals(paramActionEvent.getActionCommand()))
		{
			try {
				levelEditor.openWorld(null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			JFileChooser jfc = new JFileChooser();
			jfc.setVisible(true);
		}
		else if("addSprite".equals(paramActionEvent.getActionCommand()))
		{
			LevelEditorWorldSprite newSprite = new LevelEditorWorldSprite(levelEditor.levelEditorWorld);
			levelEditor.levelEditorWorld.updateSprite(newSprite);
			levelEditor.levelEditorWorld.select(newSprite);
		}
		else if("addPlayerSpawnPoint".equals(paramActionEvent.getActionCommand()))
		{
			LevelEditorPoint lep;
			levelEditor.levelEditorWorld.updatePlayerSpawn(lep = new LevelEditorPoint(10, 10));
			levelEditor.levelEditorWorld.select(lep);
		}
		else if("addEnemySpawnPoint".equals(paramActionEvent.getActionCommand()))
		{
			LevelEditorPoint lep;
			levelEditor.levelEditorWorld.updateMobSpawn(lep = new LevelEditorPoint(10, 10));
			levelEditor.levelEditorWorld.select(lep);
		}
		else if("addCrateSpawnZone".equals(paramActionEvent.getActionCommand()))
		{
			LevelEditorZone lep;
			levelEditor.levelEditorWorld.updateCrateSpawn(lep = new LevelEditorZone(levelEditor.levelEditorWorld, 10, 10, 10, 10));
			levelEditor.levelEditorWorld.select(lep);
		}
		else if("updateView".equals(paramActionEvent.getActionCommand()))
		{
			levelEditor.updateDrawingControls();
		}
		else if("updateView/entity".equals(paramActionEvent.getActionCommand()))
		{
			levelEditor.renderCheckbox.setSelected(false);
			levelEditor.backgroundCheckbox.setSelected(false);
			levelEditor.clipCheckbox.setSelected(true);
			levelEditor.triggerCheckbox.setSelected(true);
			levelEditor.crateSpawnZoneCheckbox.setSelected(true);
			levelEditor.playerSpawnsCheckbox.setSelected(true);
			levelEditor.mobSpawnCheckbox.setSelected(true);
			levelEditor.foregroundCheckbox.setSelected(false);
			levelEditor.updateDrawingControls();
		}
		else if("updateView/graphics".equals(paramActionEvent.getActionCommand()))
		{
			levelEditor.renderCheckbox.setSelected(true);
			levelEditor.backgroundCheckbox.setSelected(true);
			levelEditor.clipCheckbox.setSelected(false);
			levelEditor.triggerCheckbox.setSelected(false);
			levelEditor.crateSpawnZoneCheckbox.setSelected(false);
			levelEditor.playerSpawnsCheckbox.setSelected(false);
			levelEditor.mobSpawnCheckbox.setSelected(false);
			levelEditor.foregroundCheckbox.setSelected(true);
			levelEditor.updateDrawingControls();
		}
		else if(paramActionEvent.getActionCommand().startsWith("setResolution/"))
			levelEditor.levelEditorWorld.setResolution(Integer.parseInt(paramActionEvent.getActionCommand().replaceFirst("setResolution/", "")));
		else if(paramActionEvent.getActionCommand().startsWith("setBounds"))
			levelEditor.levelEditorWorld.setBounds(!levelEditor.levelEditorWorld.bounds);
	}
	
}

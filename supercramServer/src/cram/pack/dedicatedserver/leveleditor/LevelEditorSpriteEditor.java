package cram.pack.dedicatedserver.leveleditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class LevelEditorSpriteEditor extends JDialog implements KeyListener,ActionListener
{
	private static final long serialVersionUID = -2118888382974901421L;
	private LevelEditorWorldSprite sprite;
	public LevelEditorSpriteEditor(LevelEditorWorldSprite sprite)
	{
		super(sprite.world.mainFrame,"Level Editor - Sprite Edit Window");
		
		JFrame parent = sprite.world.mainFrame;
		Dimension parentSize = parent.getSize();
		Point p = parent.getLocation(); 
		setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
		setModal(true);
		this.sprite = sprite;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		
		add(buildSizeAndPositionPanel(),c);
		c.gridy = 1;
		add(buildMainPanel(),c);
		pack();
		
		setVisible(true);
	}
	ButtonGroup typeGroup = null;
	JRadioButton clipRadio;
	JRadioButton trigRadio;
	JRadioButton noncRadio;
	JCheckBox hasImageCheckBox;
	JCheckBox backgroundCheckBox;
	private JPanel buildMainPanel()
	{
		JPanel jp = new JPanel();
		
		
		
		typeGroup = new ButtonGroup();
		JRadioButton clipRadio = new JRadioButton("Clip");
		JRadioButton trigRadio = new JRadioButton("Trigger");
		JRadioButton noncRadio = new JRadioButton("Graphic");
		
		if(sprite.collisions)
			clipRadio.setSelected(true);
		else if(sprite.trigger)
			trigRadio.setSelected(true);
		if(!sprite.collisions && !sprite.trigger)
			noncRadio.setSelected(true);
		
		
		clipRadio.addActionListener(this);
		trigRadio.addActionListener(this);
		noncRadio.addActionListener(this);
		
		clipRadio.setActionCommand("setclips");
		trigRadio.setActionCommand("settrig");
		noncRadio.setActionCommand("setnonc");
		
		
				
		typeGroup.add(clipRadio);
		typeGroup.add(trigRadio);
		typeGroup.add(noncRadio);
		
		hasImageCheckBox = new JCheckBox("Image");
		hasImageCheckBox.addActionListener(this);
		hasImageCheckBox.setActionCommand("setHasImage");
		backgroundCheckBox = new JCheckBox("Background");
		backgroundCheckBox.addActionListener(this);
		backgroundCheckBox.setActionCommand("setBackground");
		
		hasImageCheckBox.setSelected(wasAnImage=sprite.hasImage);
		backgroundCheckBox.setEnabled(sprite.hasImage);
		backgroundCheckBox.setSelected(sprite.background);
		
		jp.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		jp.add(clipRadio,c);
		c.gridy = 1;
		jp.add(trigRadio,c);
		c.gridy = 2;
		jp.add(noncRadio,c);
		c.gridy = 0;
		c.gridx = 1;
		jp.add(hasImageCheckBox,c);
		c.gridy = 1;
		jp.add(backgroundCheckBox,c);
		
		return jp;
	}
	private JPanel buildSizeAndPositionPanel()
	{
		JPanel jp = new JPanel();
		
		
		xTextField = new JTextField(""+sprite.aabb.x     ,20);
		yTextField = new JTextField(""+sprite.aabb.y     ,20);
		wTextField = new JTextField(""+sprite.aabb.width ,20);
		hTextField = new JTextField(""+sprite.aabb.height,20);
		
		
		xTextField.addKeyListener(this);
		yTextField.addKeyListener(this);
		wTextField.addKeyListener(this);
		hTextField.addKeyListener(this);
		
		jp.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.gridx = 0;
		c.gridy = 0;
		jp.add(new JLabel("X:"),c);
		c.gridx = 1;
		jp.add(xTextField,c);
		c.gridx = 2;
		jp.add(new JLabel("W:"),c);
		c.gridx = 3;
		jp.add(wTextField,c);
		c.gridx = 0;
		c.gridy = 1;
		jp.add(new JLabel("Y:"),c);
		c.gridx = 1;
		jp.add(yTextField,c);
		c.gridx = 2;
		jp.add(new JLabel("H:"),c);
		c.gridx = 3;
		jp.add(hTextField,c);
		
		return jp;
	}
	JTextField xTextField = null;
	JTextField yTextField = null;
	JTextField wTextField = null;
	JTextField hTextField = null;
	@Override
	public void keyTyped(KeyEvent paramKeyEvent) {}
	@Override
	public void keyPressed(KeyEvent paramKeyEvent) {}
	@Override
	public void keyReleased(KeyEvent paramKeyEvent) {
		
		if(paramKeyEvent.getComponent() instanceof JTextField)
		{
			JTextField jtf = (JTextField)paramKeyEvent.getComponent();
			int i = -100;
			try { i = Integer.parseInt(jtf.getText()); }catch(NumberFormatException e){}
			if(jtf==xTextField)
			{
				if(sprite.world.bounds && (i<0 || (i+sprite.aabb.width)>800))
					xTextField.setBorder(new LineBorder(Color.RED));
				else
				{
					xTextField.setBorder(new LineBorder(Color.GREEN));
					sprite.aabb.x = i;
					sprite.updateSelectionField();
				}
				
			}
			if(jtf==yTextField)
			{
				if(sprite.world.bounds && (i<0 || (i+sprite.aabb.height)>600))
					yTextField.setBorder(new LineBorder(Color.RED));
				else
				{
					yTextField.setBorder(new LineBorder(Color.GREEN));
					sprite.aabb.y = i;
					sprite.updateSelectionField();
				}
			}
			if(jtf==wTextField)
			{
				if(sprite.world.bounds && (i<0 || (sprite.aabb.x+sprite.aabb.width)>800))
					yTextField.setBorder(new LineBorder(Color.RED));
				else
				{
					yTextField.setBorder(new LineBorder(Color.GREEN));
					sprite.aabb.width = i;
					sprite.updateSelectionField();
				}
			}
			if(jtf==hTextField)
			{
				if(sprite.world.bounds && (i<0 || (sprite.aabb.y+sprite.aabb.height)>600))
					yTextField.setBorder(new LineBorder(Color.RED));
				else
				{
					yTextField.setBorder(new LineBorder(Color.GREEN));
					sprite.aabb.height = i;
					sprite.updateSelectionField();
				}
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent paramActionEvent) {
		if("setclips".equals(paramActionEvent.getActionCommand()))
		{
			sprite.collisions = true;
			sprite.trigger = false;
			
			hasImageCheckBox.setSelected(sprite.hasImage=wasAnImage);
			hasImageCheckBox.setEnabled(true);
			backgroundCheckBox.setEnabled(hasImageCheckBox.isSelected());
			
			sprite.world.updateSprite(sprite);
			
		}
		else if("settrig".equals(paramActionEvent.getActionCommand()))
		{
			sprite.collisions = false;
			sprite.trigger = true;
			
			hasImageCheckBox.setSelected(sprite.hasImage=wasAnImage);
			hasImageCheckBox.setEnabled(true);
			backgroundCheckBox.setEnabled(hasImageCheckBox.isSelected());
			
			sprite.world.updateSprite(sprite);
		}
		else if("setnonc".equals(paramActionEvent.getActionCommand()))
		{
			/*hasImageCheckBox
			backgroundCheckBox*/
			sprite.collisions = false;
			sprite.trigger = false;
			sprite.hasImage = true;
			
			wasAnImage = hasImageCheckBox.isSelected();
			hasImageCheckBox.setEnabled(false);
			hasImageCheckBox.setSelected(true);
			backgroundCheckBox.setEnabled(true);
			
			sprite.world.updateSprite(sprite);
		}
		else if("setHasImage".equals(paramActionEvent.getActionCommand()))
		{
			backgroundCheckBox.setEnabled(wasAnImage=sprite.hasImage=hasImageCheckBox.isSelected());
			sprite.world.updateSprite(sprite);
		}
		else if("setBackground".equals(paramActionEvent.getActionCommand()))
		{
			sprite.background = backgroundCheckBox.isSelected();
			sprite.world.updateSprite(sprite);
		}
	}
	boolean wasAnImage = false;
}

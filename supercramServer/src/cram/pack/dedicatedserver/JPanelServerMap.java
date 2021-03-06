package cram.pack.dedicatedserver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;

public class JPanelServerMap extends JPanel implements KeyListener, MouseListener
{
	World world;
	public JPanelServerMap(World pWorld)
	{
		setLocation(0, 0);
		world = pWorld;
		setBackground(Color.WHITE);
		setSize(800, 600);
	}
	private static final long serialVersionUID = 8999627801799066274L;
	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, 800, 600);
		g.setColor(Color.GREEN);
		for(Rectangle r : world.background)
			g.fillRect(r.x, r.y, r.width, r.height);
		g.setColor(Color.BLUE);
		for(Player p : world.players)
			g.fillRect(p.aabb.x, p.aabb.y, p.aabb.width, p.aabb.height);
		g.setColor(Color.RED);
		for(Entity p : world.enemies)
			g.fillRect(p.aabb.x, p.aabb.y, p.aabb.width, p.aabb.height);
		g.setColor(Color.ORANGE);
		for(Entity p : world.crates)
			g.fillRect(p.aabb.x, p.aabb.y, p.aabb.width, p.aabb.height);
		g.setColor(Color.DARK_GRAY);
		
		for(Rectangle r : world.foreground)
			g.fillRect(r.x, r.y, r.width, r.height);
		if(selectedEntity!=null)
			g.drawString("!", selectedEntity.aabb.x, selectedEntity.aabb.y);
		
		g.drawString("fps="+world.serv.fps.get(), 100, 100);
		try{Thread.sleep( (lastLoopTime-System.nanoTime() + OPTIMAL_TIME)/1000000);}catch(Exception e){}
		lastLoopTime = System.nanoTime();
		repaint();
	}
	long lastLoopTime = System.nanoTime();
	final int TARGET_FPS = 39;
	final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	Entity selectedEntity = null;
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {
		char c = e.getKeyChar();
		switch(c)
		{
		case 'c':
		case 's':
			key.set(c);
			break;
		}
	}
	AtomicInteger key = new AtomicInteger();
	public void processKey()
	{
		char c = (char)key.getAndSet(0);
		switch(c)
		{
		case 'c':
			world.dropCrate();
			break;
		case 's':
			world.dropEnemy();
			break;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		selectedEntity = null;
		for(Entity ent : world.getEntities())
		{
			if(ent.aabb.contains(e.getX(), e.getX()))
				selectedEntity = ent;
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}

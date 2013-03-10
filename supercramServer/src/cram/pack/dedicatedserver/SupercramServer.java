package cram.pack.dedicatedserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import cram.pack.dedicatedserver.leveleditor.LevelEditor;

public class SupercramServer extends Thread
{
	private Logger LOGGER = null;
	public SupercramServer(ServerConfigurationManager cfg) throws IOException
	{
		LOGGER = Logger.getLogger(cfg.getName());
		config=cfg;
		manager = new ConnectionManager(this);
	}
	private ConnectionManager manager;
	private ServerConfigurationManager config = null;
	public ConnectionManager getConnectionManager(){return manager;}
	public ServerConfigurationManager getConfiguration(){return config;}
	private LinkedList<NetServerHandler> players = new LinkedList<NetServerHandler>();
	public void run()
	{
		loadWorlds();
		while(!ShutdownServer)
		{
			// Load incoming connections
			handleIncomingConnections();
			// Tick Worlds
			for(World w : worlds)
				w.update();
			// Tick Players
			Iterator<NetServerHandler> nshI = players.iterator();
			while(nshI.hasNext())
			{
				NetServerHandler nsh = nshI.next();
				nsh.tick();
				if(nsh.disconnected)
					nshI.remove();
			}
			tickRateManage();
		}
	}
	private void handleIncomingConnections()
	{
		NetServerHandler nsh = pendingConnections.poll();
		if(nsh==null)
			return;
		Iterator<NetServerHandler> nshI = players.iterator();
		while(nshI.hasNext())
		{
			NetServerHandler activeNSH = nshI.next();
			if(activeNSH.username.equals(nsh.username))
			{
				activeNSH.kick("You logged in from another location!");
				nshI.remove();
			}
		}
		LOGGER.info(nsh.username+" joined the server");
		nsh.changeWorld(getWorld("world"));
		players.add(nsh);
	}
	public World getWorld(String worldName)
	{
		return worlds.get(0);
	}
	public final boolean ShutdownServer = false;
	public AtomicInteger fps = new AtomicInteger();
	
	long lastLoopTime = System.nanoTime();
	final int TARGET_FPS = 39;
	final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	void tickRateManage()
	{
		//long currentTick = System.nanoTime();
		//long callingDelay = currentTick-lastLoopTime;
		long nanos = (lastLoopTime-System.nanoTime() + OPTIMAL_TIME);
		long milis = nanos/1000000;
		nanos = (milis*1000000)/1000;
		try{Thread.sleep(milis,(int)nanos);}catch(Exception e){}
		lastLoopTime = System.nanoTime();
		

		fps.set((int)(1000000000/(System.nanoTime()-lastFPSTime)));
		lastFPSTime = System.nanoTime();
	}
	long lastFPSTime = System.nanoTime();
	private ArrayList<World> worlds = new ArrayList<World>(1);
	public static ConcurrentLinkedQueue<NetServerHandler> pendingConnections = new ConcurrentLinkedQueue<NetServerHandler>();
	private void loadWorlds()
	{
		LOGGER.info("Loading worlds...");
		File[] worldFiles = config.getWorldFiles();
		for(File worldFile : worldFiles)
		{
			World w = null;
			try{w = new World(this,worldFile);}catch(Exception e){e.printStackTrace();}
			if(w!=null)
			{
				LOGGER.info("Loaded world '"+w.name+"'");
				worlds.add(w);
			}
			else
				LOGGER.info("Failed to load world '"+worldFile.getName()+"'");
		}
		LOGGER.info("Worlds loaded");
	}
}

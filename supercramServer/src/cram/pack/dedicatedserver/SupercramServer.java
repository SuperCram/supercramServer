package cram.pack.dedicatedserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SupercramServer extends Thread
{
	public SupercramServer(ServerConfigurationManager cfg) throws IOException
	{
		config=cfg;
		manager = new ConnectionManager(this);
	}
	private ConnectionManager manager;
	private ServerConfigurationManager config = null;
	public ConnectionManager getConnectionManager(){return manager;}
	public ServerConfigurationManager getConfiguration(){return this.config;}
	private LinkedList<NetServerHandler> players = new LinkedList<NetServerHandler>();
	public void run()
	{
		loadWorlds();
		while(!ShutdownServer)
		{
			handleIncomingConnections();
			tickWorlds();
			tickRateManage();
			tickPlayers();
		}
	}
	private void tickPlayers()
	{
		Iterator<NetServerHandler> nshI = players.iterator();
		while(nshI.hasNext())
		{
			NetServerHandler nsh = nshI.next();
			nsh.tick();
			if(nsh.disconnected)
				nshI.remove();
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
		players.add(nsh);
	}
	public final boolean ShutdownServer = false;
	public static final int targetFPS = 35;
	public static final int tickDelay = (int)(((float)1000/targetFPS));
	private long lastTick = -1;
	void tickRateManage()
	{
		if(lastTick==-1)
			lastTick = System.currentTimeMillis();
		long timeDifference = System.currentTimeMillis()-lastTick;
		try
		{
			int delayRate = (int) (tickDelay-timeDifference);
			if(delayRate>0)
			{
				Thread.currentThread();
				Thread.sleep(delayRate);
			}
			else
			{
				throw new Exception("Server is overloaded");
			}
		}
		catch(Exception e){}
	}
	private ArrayList<World> worlds = new ArrayList<World>(1);
	private void tickWorlds()
	{
		for(World w : worlds)
			w.tick();
	}
	public static ConcurrentLinkedQueue<NetServerHandler> pendingConnections = new ConcurrentLinkedQueue<NetServerHandler>();
	private void loadWorlds()
	{
		File[] worldFiles = config.getWorldFiles();
		for(File worldFile : worldFiles)
		{
			World w = null;
			try{w = new World(worldFile);}catch(Exception e){}
			if(w!=null)
				worlds.add(w);
		}
	}
}

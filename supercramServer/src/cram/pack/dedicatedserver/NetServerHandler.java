package cram.pack.dedicatedserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Random;

import cram.pack.dedicatedserver.protocol.Packet;
import cram.pack.dedicatedserver.protocol.Packet10WorldHashRequest;
import cram.pack.dedicatedserver.protocol.Packet11WorldHashResponse;
import cram.pack.dedicatedserver.protocol.Packet12WorldData;
import cram.pack.dedicatedserver.protocol.Packet13WorldChange;
import cram.pack.dedicatedserver.protocol.Packet14WorldDidChange;
import cram.pack.dedicatedserver.protocol.Packet3Kick;
import cram.pack.dedicatedserver.protocol.Packet6KeepAlive;

public class NetServerHandler
{
	String username = "";
	public NetServerHandler(){}
	Player player = null;
	SupercramServer server = null;
	NetEntityTracker entityTracker = null;
	Socket socket = null;
	public NetServerHandler(SupercramServer pServer,Socket pSocket,String pUsername, DataInputStream dis, DataOutputStream dos)
	{
		server = pServer;
		socket = pSocket;
		username = pUsername;
		reader = new NetReadThread(this, dis);
		reader.start();
		writer = new NetWriteThread(this, dos);
		writer.start();
		entityTracker = new NetEntityTracker(this);
		updateConfig();
	}
	public void handle(Packet p)
	{
		kick("Unauthorised packet!");
	}
	//public void handle(Packet0StatusRequest p){}
	//public void handle(Packet1Status p){}
	//public void handle(Packet2Login p){}
	//public void handle(Packet3Kick p){}
	//public void handle(Packet4Logout p){}
	//public void handle(Packet5LoginSucess p){}
	private long lastKeepAliveSentMils = -1;
	private long lastKeepAliveUID = -1;
	private long maxKeepAliveMils = 10000;
	public void handle(Packet6KeepAlive packet6KeepAlive)	{ lastKeepAliveUID = -1; }
	public void kick(String reason)
	{
		System.out.println("Kicking player"+reason);
		disconnected = true;
		sendPacket(new Packet3Kick(reason));
	}
	public void tick()
	{
		if(disconnected)
		{
			// Cleanup
			try { reader.close(); } catch(Exception e) { }
			try { writer.close(); } catch(Exception e) { }
			return;
		}
		// Keep alive
		long dif = (System.currentTimeMillis()-lastKeepAliveSentMils);
		//System.out.println(dif);
		if(lastKeepAliveSentMils==-1 || dif>maxKeepAliveMils)
		{
			if(lastKeepAliveUID!=-1 && lastKeepAliveSentMils!=-1)
				kick("Ping too high");
			else
			{
				maxKeepAliveMils = 1000*config_max_ping;
				sendPacket(new Packet6KeepAlive(Long.toString(lastKeepAliveUID=new Random().nextLong())));
				
				lastKeepAliveSentMils = System.currentTimeMillis();
			}
		}
		
		entityTracker.update();
		// Read from packet queue
		
		for(int i=0;i<config_packet_read_count;i++)
		{
			Packet p = reader.readPacket();
			if(p!=null) p.handle(this);
		}
	}
	// World
	World targetWorld = null;
	World currentWorld = null;
	public void changeWorld(World w)
	{
		if(currentWorld==null)
			player = new Player(username,this);
		if(w==currentWorld)
			return;
		targetWorld = w;
		sendPacket(new Packet10WorldHashRequest(w.name));
	}
	//public void handle(Packet10WorldHashRequest packet10WorldHashRequest) {}
	public void handle(Packet11WorldHashResponse packet11WorldHashResponse) {
		if(!targetWorld.name.equals(packet11WorldHashResponse.worldName))
			return;
		if(!targetWorld.hash.equals(packet11WorldHashResponse.worldHash))
			sendPacket(new Packet12WorldData(targetWorld.name, targetWorld.worldFile));
		else
			sendPacket(new Packet13WorldChange(targetWorld.name));
			
	}
	//public void handle(Packet12WorldData packet12WorldData){}
	//public void handle(Packet13WorldChange packet13WorldChange){}
	public void handle(Packet14WorldDidChange packet14WorldDidChange)
	{
		if(targetWorld.name.equals(packet14WorldDidChange.worldName))
		{
			currentWorld = targetWorld;
			entityTracker.clearEntities();
			targetWorld.addEntity(player);
			player.setXY(targetWorld.getRandomPlayerSpawn());
			
			targetWorld = null;
		}
	}
	// Packets
	
	NetReadThread reader = null;
	NetWriteThread writer = null;
	public boolean disconnected = false;
	public void sendPacket(Packet packet)
	{
		writer.addPacket(packet);
	}
	// Config
	private int config_max_ping = 10;
	private int config_packet_read_count = 0;
	public void updateConfig()
	{
		//config_max_ping = Math.min(1, Math.max(60, server.getConfiguration().getInt("max-ping", 10)));
		config_packet_read_count = Math.min(5, Math.max(60, server.getConfiguration().getInt("packet-read-count", 5)));
	}
	Weapon currentWeapon = null;
	// Weapons
	public void changeWeapon(Weapon weap)
	{
		if(currentWeapon!=weap)
		{
			currentWeapon = weap;
			// currentWorld.sendPacketToPlayers(new Packet#WeaponChange(weap.getID()));
		}
	}
}

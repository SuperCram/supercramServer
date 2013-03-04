package cram.pack.dedicatedserver;

import java.net.Socket;
import java.util.Random;

import cram.pack.dedicatedserver.protocol.Packet;
import cram.pack.dedicatedserver.protocol.Packet0StatusRequest;
import cram.pack.dedicatedserver.protocol.Packet10WorldHashRequest;
import cram.pack.dedicatedserver.protocol.Packet11WorldHashResponse;
import cram.pack.dedicatedserver.protocol.Packet12WorldData;
import cram.pack.dedicatedserver.protocol.Packet13WorldChange;
import cram.pack.dedicatedserver.protocol.Packet14WorldDidChange;
import cram.pack.dedicatedserver.protocol.Packet1Status;
import cram.pack.dedicatedserver.protocol.Packet2Login;
import cram.pack.dedicatedserver.protocol.Packet3Kick;
import cram.pack.dedicatedserver.protocol.Packet4Logout;
import cram.pack.dedicatedserver.protocol.Packet5LoginSucess;
import cram.pack.dedicatedserver.protocol.Packet6KeepAlive;

public class NetServerHandler
{
	String username = "";
	public NetServerHandler(){}
	Player player = null;
	SupercramServer server = null;
	Socket socket = null;
	public NetServerHandler(SupercramServer pServer,Socket pSocket,String pUsername)
	{
		server = pServer;
		socket = pSocket;
		username = pUsername;
		updateConfig();
	}
	public void handle(Packet0StatusRequest p)			{ kick("You asked the server how it was, KICK!"); }
	public void handle(Packet1Status p)						{ kick("You told the server how the server was"); }
	public void handle(Packet2Login p)							{ kick("You tried to login again"); }
	public void handle(Packet3Kick p)							{ kick("You tried to kick me, THIS IS SPARTAAA!"); }
	public void handle(Packet4Logout p)						{ kick(p.reason); }
	public void handle(Packet5LoginSucess p)				{ kick("You told me you logged in sucessfully"); }
	private long lastKeepAliveSentMils = -1;
	private long lastKeepAliveUID = -1;
	private long maxKeepAliveMils = 1000;
	public void handle(Packet6KeepAlive packet6KeepAlive)	{ lastKeepAliveUID = -1; }
	public void kick(String reason)
	{
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
		if((System.currentTimeMillis()-lastKeepAliveSentMils)>maxKeepAliveMils)
		{
			if(lastKeepAliveUID==-1)
				kick("Ping too high");
			else
			{
				maxKeepAliveMils = 1000*config_max_ping;
				sendPacket(new Packet6KeepAlive(lastKeepAliveUID=new Random().nextLong()));
				lastKeepAliveSentMils = System.currentTimeMillis();
			}
		}
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
		if(w==currentWorld)
			return;
		targetWorld = w;
		sendPacket(new Packet10WorldHashRequest(w.name));
	}
	public void handle(Packet10WorldHashRequest packet10WorldHashRequest) {
		kick("Sent a Packet10WorldHashRequest");
	}
	public void handle(Packet11WorldHashResponse packet11WorldHashResponse) {
		if(!targetWorld.name.equals(packet11WorldHashResponse.worldName))
			return;
		if(!targetWorld.hash.equals(packet11WorldHashResponse.worldHash))
			sendPacket(new Packet12WorldData(targetWorld.hash, targetWorld.tag));
		else
			sendPacket(new Packet13WorldChange(targetWorld.name));
			
	}
	public void handle(Packet12WorldData packet12WorldData) { kick("Sent a Packet12WorldData"); }
	public void handle(Packet13WorldChange packet13WorldChange) { kick("Sent a Packet13WorldChange"); }
	public void handle(Packet14WorldDidChange packet14WorldDidChange)
	{
		if(targetWorld.name.equals(packet14WorldDidChange.worldName))
		{
			targetWorld = null;
			currentWorld = targetWorld;
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
		config_max_ping = Math.min(1, Math.max(60, server.getConfiguration().getInt("max-ping", 10)));
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

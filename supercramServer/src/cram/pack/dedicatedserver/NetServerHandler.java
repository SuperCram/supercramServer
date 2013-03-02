package cram.pack.dedicatedserver;

import java.net.Socket;

import cram.pack.dedicatedserver.protocol.Packet;
import cram.pack.dedicatedserver.protocol.Packet0StatusRequest;
import cram.pack.dedicatedserver.protocol.Packet1Status;
import cram.pack.dedicatedserver.protocol.Packet2Login;
import cram.pack.dedicatedserver.protocol.Packet3LoginFailed;
import cram.pack.dedicatedserver.protocol.Packet4LoginSucess;
import cram.pack.dedicatedserver.protocol.Packet5Kick;
import cram.pack.dedicatedserver.protocol.Packet6KeepAlive;
import cram.pack.dedicatedserver.protocol.Packet7Logout;

public class NetServerHandler
{
	String username = "";
	public NetServerHandler(){}
	Player player = null;
	Socket socket = null;
	public NetServerHandler(Player p,Socket s,String user)
	{
		player=p;
		socket=s;
		username = user;
	}
	public void handlePacket0StatusRequest(Packet0StatusRequest p)
	{
		
	}
	public void handlePacket1Status(Packet1Status p)
	{
		kick("Protocol Mismatch!");
	}
	public void handlePacket2Login(Packet2Login p)
	{
		kick("Duplicate login attempt");
	}
	public void handlePacket3LoginFailed(Packet3LoginFailed p)
	{
		kick("Protocol Mismatch!");
	}
	public void handlePacket4LoginSucess(Packet4LoginSucess p)
	{
		kick("Protocol Mismatch!");
	}
	public void handlePacket5Kick(Packet5Kick p)
	{
		kick("Protocol Mismatch!");
	}
	public void handlePacket6KeepAlive(Packet6KeepAlive p)
	{
		
	}
	public void handlePacket7Logout(Packet7Logout p)
	{
		kick("EXIT");
	}
	public void kick(String reason)
	{
		sendPacket(new Packet5Kick(reason));
		disconnected = true;
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
		for(int i=0;i<5;i++)
		{
			Packet p = reader.readPacket();
			if(p!=null) p.handle(this);
		}
	}
	
	NetReadThread reader = null;
	NetWriteThread writer = null;
	public boolean disconnected = false;
	public void sendPacket(Packet packet)
	{
		writer.addPacket(packet);
	}
}

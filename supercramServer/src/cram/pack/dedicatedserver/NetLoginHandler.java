package cram.pack.dedicatedserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import cram.pack.dedicatedserver.protocol.Packet;
import cram.pack.dedicatedserver.protocol.Packet0StatusRequest;
import cram.pack.dedicatedserver.protocol.Packet1Status;
import cram.pack.dedicatedserver.protocol.Packet2Login;
import cram.pack.dedicatedserver.protocol.Packet3Kick;
import cram.pack.dedicatedserver.protocol.Packet5LoginSucess;

public class NetLoginHandler extends Thread
{
	Socket socket = null;
	boolean deleted = false;
	SupercramServer server = null;
	public NetLoginHandler(SupercramServer serv, Socket s)
	{
		server=serv;
		socket=s;
		start();
	}
	public void run()
	{
		try
		{
			socket.setSoTimeout(1000);
			handle();
		}
		catch(Exception e)
		{
			try { socket.close(); } catch(Exception ee) { }
			deleted=true;
		}
	}
	public static final int ServerVersion = 0;
	public static final int ProtocolVersion = 0;
	public void handle() throws Exception
	{
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		while(true)
		{
			Packet p = Packet.readPacket(dis.readByte());
			p.readDIS(dis);
			if(p instanceof Packet0StatusRequest)
			{
				Packet1Status p1s = new Packet1Status();
				p1s.ServerVersion = ServerVersion;
				p1s.ProtocolVersion = ProtocolVersion;
				
				p1s.ServerName = server.getConfiguration().getString("server-name", "Unnamed server");
				p1s.Players = 1;
				p1s.MaxPlayers = 6;
				p=p1s;
			}
			else if(p instanceof Packet2Login)
			{
				Packet2Login p2l = (Packet2Login)p;
				if(p2l.protocolVersion!=ProtocolVersion)
				{
					Packet3Kick p3lf = new Packet3Kick();
					p3lf.reason = "Outdated "+(ProtocolVersion>p2l.protocolVersion ? "Client!" : "Server!");
					p = p3lf;
				}
				else
				{
					byte b = server.getConfiguration().checkLogin(p2l.username, p2l.password);
					// 1 - Bad Username
					// 2 - Bad Password
					// 3 - Sucess
					if(b==3)
					{
						Packet5LoginSucess p4ls=new Packet5LoginSucess();
						p = p4ls;
						NetServerHandler nsh = new NetServerHandler(server, socket, p2l.username);
						SupercramServer.pendingConnections.add(nsh);
					}
					else
					{
						Packet3Kick p3lf = new Packet3Kick();
						if(b==1)
							p3lf.reason = "Unknown Username";
						else
							p3lf.reason = "Bad Password";
						p = p3lf;
					}
				}
			}
			else
				p = null;
			dos.writeByte(p.PacketID);
			p.writeDOS(dos);
		}
	}
}
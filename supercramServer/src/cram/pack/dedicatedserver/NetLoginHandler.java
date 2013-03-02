package cram.pack.dedicatedserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import cram.pack.dedicatedserver.protocol.Packet;
import cram.pack.dedicatedserver.protocol.Packet0StatusRequest;
import cram.pack.dedicatedserver.protocol.Packet1Status;
import cram.pack.dedicatedserver.protocol.Packet2Login;
import cram.pack.dedicatedserver.protocol.Packet3LoginFailed;
import cram.pack.dedicatedserver.protocol.Packet4LoginSucess;

public class NetLoginHandler extends Thread
{
	Socket socket = null;
	boolean deleted = false;
	CRAMTheServer server = null;
	public NetLoginHandler(CRAMTheServer serv, Socket s)
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
				if(p2l.ProtocolVersion!=ProtocolVersion)
				{
					Packet3LoginFailed p3lf = new Packet3LoginFailed();
					p3lf.reason = "Outdated "+(ProtocolVersion>p2l.ProtocolVersion ? "Client!" : "Server!");
					p = p3lf;
				}
				else
				{
					byte b = server.getConfiguration().checkLogin(p2l.username, p2l.password);
					// 3 - Bad Both
					// 4 - Valid
					// 5 - Admin
					if(b<4)
					{
						Packet3LoginFailed p3lf = new Packet3LoginFailed();
						if(b==1)
							p3lf.reason = "Unknown username";
						else if(b==2)
							p3lf.reason = "Wrong password";
						else if(b==3)
							p3lf.reason = "Bad login details";
						p = p3lf;
					}
					else
					{
						Packet4LoginSucess p4ls=new Packet4LoginSucess();
						p4ls.isAdmin = (b==5);
						p = p4ls;
						
						Player player = new Player(p2l.username, b==5);
						NetServerHandler nsh = new NetServerHandler(player, socket, p2l.username);
						CRAMTheServer.pendingConnections.add(nsh);
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

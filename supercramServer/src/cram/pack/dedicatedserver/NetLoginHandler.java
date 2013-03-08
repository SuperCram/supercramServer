package cram.pack.dedicatedserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import cram.pack.dedicatedserver.protocol.Packet;
import cram.pack.dedicatedserver.protocol.Packet0StatusRequest;
import cram.pack.dedicatedserver.protocol.Packet1Status;
import cram.pack.dedicatedserver.protocol.Packet2Login;
import cram.pack.dedicatedserver.protocol.Packet3Kick;
import cram.pack.dedicatedserver.protocol.Packet5LoginSucess;

public class NetLoginHandler extends Thread
{
	private final static Logger LOGGER = Logger.getLogger(ConnectionManager.class.getSimpleName());
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
			e.printStackTrace();
			try { socket.close(); } catch(Exception ee) { }
			deleted=true;
		}
	}
	
	public static final int ServerVersion = 1;
	public static final int ProtocolVersion = 1;
	public void handle() throws Exception
	{
		DataInputStream dis = new DataInputStream(socket.getInputStream());
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		Packet p = Packet.createPacket(dis.readByte());
		p.readDIS(dis);
		LOGGER.info("Incoming packet:"+p);
		if(p instanceof Packet0StatusRequest)
		{
			Packet1Status p1s = new Packet1Status();
			p1s.ServerVersion = ServerVersion;
			p1s.ProtocolVersion = ProtocolVersion;
			
			p1s.ServerName = server.getConfiguration().getString("server-name", "Unnamed server");
			p1s.Players = 1;
			p1s.MaxPlayers = 6;
			
			dos.writeByte(p1s.PacketID);
			p1s.writeDOS(dos);
			dos.flush();
		}
		else if(p instanceof Packet2Login)
		{
			LOGGER.info("P2l");
			Packet2Login p2l = (Packet2Login)p;
			if(p2l.protocolVersion!=ProtocolVersion)
			{
				Packet3Kick p3lf = new Packet3Kick("Outdated "+(ProtocolVersion>p2l.protocolVersion ? "Client!" : "Server!"));
				
				dos.writeByte(p3lf.PacketID);
				p3lf.writeDOS(dos);
				dos.flush();
				LOGGER.info("OUTDATED:"+ProtocolVersion+p2l.protocolVersion);
			}
			else
			{
				//byte b = server.getConfiguration().checkLogin(p2l.username, p2l.password);
				byte b = 3;
				// 1 - Bad Username
				// 2 - Bad Password
				// 3 - Sucess
				if(b==3)
				{
					LOGGER.info("YES");
					
					Packet5LoginSucess p4ls=new Packet5LoginSucess();
					dos.writeByte(5);
					//p4ls.writeDOS(dos);
					dos.flush();
					
					
					NetServerHandler nsh = new NetServerHandler(server, socket, p2l.username, dis, dos);
					SupercramServer.pendingConnections.add(nsh);
				}
				else
				{
					LOGGER.info("KICK");
					Packet3Kick p3lf = new Packet3Kick();
					if(b==1)
						p3lf.reason = "Unknown Username";
					else
						p3lf.reason = "Bad Password";
					p = p3lf;
					
					dos.writeByte(p3lf.PacketID);
					p3lf.writeDOS(dos);
					dos.flush();
				}
			}
		}
	}
}
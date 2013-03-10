package cram.pack.dedicatedserver;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import cram.pack.dedicatedserver.protocol.Packet;

public class NetReadThread extends Thread
{
	private ConcurrentLinkedQueue<Packet> recvQueue = new ConcurrentLinkedQueue<Packet>();
	public void addPacket(Packet p)
	{
		if(p!=null)
			recvQueue.add(p);
	}
	public Packet readPacket()
	{
		return recvQueue.poll();
	}
	DataInputStream dis = null;
	NetServerHandler handler = null;
	public NetReadThread(NetServerHandler netserverhandler, DataInputStream datainputstream)
	{
		handler = netserverhandler;
		dis = datainputstream;
	}
	public void run()
	{
		while(!handler.disconnected)
		{
			Packet p = null;
			try
			{
				p = Packet.readPacket(dis);
				//System.out.println("Received packet: "+p);
				if(p!=null)
					addPacket(p);
				else
					handler.kick("Protocol Error! Bad packet id");
			}
			catch(EOFException e)
			{
				handler.kick("End of readable Stream");
				return;
			}
			catch(IOException e)
			{
				
				continue;
			}
			catch(Exception e)
			{
				handler.kick("Protocol Error! Failed to handle packet: "+p);
				return;
			}
		}
	}
	public void close() {
		
	}
}

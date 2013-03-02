package cram.pack.dedicatedserver;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import cram.pack.dedicatedserver.protocol.Packet;

public class NetWriteThread extends Thread
{
	private ConcurrentLinkedQueue<Packet> sendQueue = new ConcurrentLinkedQueue<Packet>();
	private DataOutputStream dos = null;
	private NetServerHandler handler = null;
	public void addPacket(Packet p)
	{
		if(p!=null)
			sendQueue.add(p);
	}
	public NetWriteThread(NetServerHandler n, DataOutputStream d)
	{
		handler = n;
		dos = d;
		start();
	}
	public void run()
	{
		while(!handler.disconnected)
		{
			Packet p = sendQueue.poll();
			if(p==null)
				continue;
			try
			{
				dos.writeByte(p.PacketID);
				p.writeDOS(dos);
			}
			catch(EOFException e)
			{
				handler.disconnected = true;
				return;
			}
			catch(IOException e)
			{
				handler.disconnected = true;
				return;
			}
			catch(Exception e)
			{
				handler.kick("Protocol Error! Failed to send packet: "+p.PacketID);
				return;
			}
		}
	}
	public void close() {
		
	}
}

package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet6KeepAlive extends Packet {
	public Packet6KeepAlive()
	{
		this(-1);
	}
	public Packet6KeepAlive(long l) {
		PacketID=6;
		UID = l;
	}
	public long UID;
	@Override
	void read(DataInputStream dis) throws IOException {
		UID = dis.readLong();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeLong(UID);
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
}

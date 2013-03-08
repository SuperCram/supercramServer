package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet22EntityDestory extends Packet {
	public Packet22EntityDestory()
	{
		PacketID = 22;
	}
	public Packet22EntityDestory(int UID)
	{
		this();
		this.UID = UID;
	}
	public int UID = 0;
	@Override
	void read(DataInputStream dis) throws IOException {
		UID = dis.readInt();
	}

	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(UID);
	}

	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	
}

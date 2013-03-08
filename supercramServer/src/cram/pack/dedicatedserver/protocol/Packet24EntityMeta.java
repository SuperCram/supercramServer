package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet24EntityMeta extends Packet {
	public Packet24EntityMeta()
	{
		throw new IllegalArgumentException("Packet cannot be read");
	}
	public Packet24EntityMeta(boolean gravity, boolean clip)
	{
		PacketID = 24;
	}
	public int UID;
	public EntityMetaTuple emt;
	
	
	@Override
	void read(DataInputStream dis) throws IOException {
		UID = dis.readInt();
		emt = new EntityMetaTuple(dis);
	}

	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(UID);
		emt.write(dos);
	}

	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	
}

package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet23EntityMove extends Packet {
	public Packet23EntityMove()
	{
		PacketID = 23;
	}
	public Packet23EntityMove(int UID, EntityPositionTuple ept)
	{
		this();
		this.UID = UID;
		this.ept = ept;
	}
	public int UID;
	public EntityPositionTuple ept;
	@Override
	void read(DataInputStream dis) throws IOException {
		UID = dis.readInt();
		ept = new EntityPositionTuple(dis);
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(UID);
		ept.write(dos);
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	
}

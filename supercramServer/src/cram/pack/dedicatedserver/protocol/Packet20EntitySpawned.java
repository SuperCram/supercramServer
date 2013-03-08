package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet20EntitySpawned extends Packet
{
	public Packet20EntitySpawned()
	{
		PacketID=20;
	}
	public Packet20EntitySpawned(int pUID, byte pType, EntityPositionTuple pEpt)
	{
		this();
		UID = pUID;
		type = pType;
		ept = pEpt;
	}
	public int UID;
	public byte type;
	public EntityPositionTuple ept;
	public EntityMetaTuple emt;
	
	@Override
	void read(DataInputStream dis) throws IOException {
		UID = dis.readInt();
		type = dis.readByte();
		ept = new EntityPositionTuple(dis);
		if(ept.hasMeta())
			emt = new EntityMetaTuple(dis);
	}

	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(UID);
		dos.writeByte(type);
		ept.write(dos);
		if(ept.hasMeta())
			emt.write(dos);
	}

	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	
	@Override
	public String toString() {
		return "Packet20EntitySpawned(UID="+UID+",type="+type+",ept="+ept+"emt="+emt+")";
	}
	
}

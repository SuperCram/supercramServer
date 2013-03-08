package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;
import cram.pack.dedicatedserver.cereal.Tag;
import cram.pack.dedicatedserver.cereal.TagMap;

public class Packet21EntitySpawnFX extends Packet {

	public Packet21EntitySpawnFX()
	{
		PacketID = 21;
	}
	public Packet21EntitySpawnFX(EntityPositionTuple ept, TagMap effectTag)
	{
		this();
		this.ept = ept;
		this.effectTag = effectTag;
	}
	public EntityPositionTuple ept;
	public TagMap effectTag;
	
	@Override
	void read(DataInputStream dis) throws IOException {
		ept = new EntityPositionTuple(dis);
		effectTag = (TagMap)Tag.readTag(dis);
	}

	@Override
	void write(DataOutputStream dos) throws IOException {
		ept.write(dos);
	}

	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
}

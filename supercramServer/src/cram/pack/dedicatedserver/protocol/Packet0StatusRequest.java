package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet0StatusRequest extends Packet{
	public Packet0StatusRequest() {
		PacketID=0;
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	@Override
	void read(DataInputStream dis) throws IOException {}
	@Override
	void write(DataOutputStream dos) throws IOException {}
}

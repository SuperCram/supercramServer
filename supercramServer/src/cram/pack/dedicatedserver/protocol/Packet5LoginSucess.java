package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet5LoginSucess extends Packet {
	public Packet5LoginSucess()
	{
		PacketID=5;
	}
	public Packet5LoginSucess(String reason2) {
		PacketID=5;
	}
	@Override
	void read(DataInputStream dis) throws IOException {}
	@Override
	void write(DataOutputStream dos) throws IOException {}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
}

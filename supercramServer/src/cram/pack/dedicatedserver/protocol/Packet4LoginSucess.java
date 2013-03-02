package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet4LoginSucess extends Packet
{
	public Packet4LoginSucess() {
		PacketID=4;
	}
	public boolean isAdmin;
	@Override
	void read(DataInputStream dis) throws IOException {
		isAdmin=dis.readBoolean();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeBoolean(isAdmin);
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handlePacket4LoginSucess(this);
	}
}

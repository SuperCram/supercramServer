package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cram.pack.dedicatedserver.NetServerHandler;

public class Packet6KeepAlive extends Packet {
	public Packet6KeepAlive()
	{
		this("");
	}
	public Packet6KeepAlive(String u) {
		PacketID=6;
		UID = u;
	}
	private String UID;
	@Override
	void read(DataInputStream dis) throws IOException {
		UID = "";
		int len = dis.readInt();
		for(int i=0;i<len;i++)
			UID+=dis.readChar();
	}
	@Override
	void write(DataOutputStream dos) throws IOException {
		dos.writeInt(UID.length());
		for(int i=0;i<UID.length();i++)
			dos.writeChar(UID.charAt(i));
	}
	@Override
	public void handle(NetServerHandler handler) {
		handler.handle(this);
	}
	@Override
	public String toString() {
		return "Packet6KeepAlive(UID="+UID+")";
	}
}

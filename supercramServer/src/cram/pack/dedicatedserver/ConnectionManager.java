package cram.pack.dedicatedserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager extends Thread
{
	boolean connectionRunning = false;
	ServerSocket socket = null;
	ServerConfigurationManager config = null;
	private CRAMTheServer server = null;
	public ConnectionManager(CRAMTheServer serv) throws IOException
	{
		config = serv.getConfiguration();
		connectionRunning = true;
		socket = new ServerSocket(config.getInt("server-port", 2059));
		start();
	}
	long lastConnection = -1;
	byte[] lastIP = new byte[]{0,0,0,0};
	public void run()
	{
		if(socket!=null)
		{
			lastConnection = System.currentTimeMillis();
			while(connectionRunning)
			{
				try
				{
					Socket sock = socket.accept();
					byte[] currentIP = sock.getInetAddress().getAddress();
					if((System.currentTimeMillis()-lastConnection)<5000 && (currentIP[0]==lastIP[0] && currentIP[1]==lastIP[1] && currentIP[2]==lastIP[2] && currentIP[3]==lastIP[3]))
					{
						sock.getOutputStream().write(7);
						sock.close();
						continue;
					}
					lastIP = currentIP;
					lastConnection = System.currentTimeMillis();
					new NetLoginHandler(server, sock);
				}
				catch(Exception e1){continue;}
			}
		}
	}
}

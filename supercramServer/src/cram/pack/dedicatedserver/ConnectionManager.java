package cram.pack.dedicatedserver;

import java.io.IOException;
import java.net.ServerSocket;

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
	public void run()
	{
		if(socket!=null)
		{
			lastConnection = System.currentTimeMillis();
			while(connectionRunning)
			{
				try
				{
					if((System.currentTimeMillis()-lastConnection)<1000)
					{
						lastConnection = System.currentTimeMillis();
						Thread.currentThread();
						Thread.sleep(100);
					}
					new NetLoginHandler(server, socket.accept());
				}
				catch(Exception e1){continue;}
			}
		}
	}
}

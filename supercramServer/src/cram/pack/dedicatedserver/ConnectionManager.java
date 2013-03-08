package cram.pack.dedicatedserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionManager extends Thread
{
	private final static Logger LOGGER = Logger.getLogger(ConnectionManager.class.getSimpleName());
	boolean connectionRunning = false;
	ServerSocket socket = null;
	ServerConfigurationManager config = null;
	private SupercramServer server = null;
	public ConnectionManager(SupercramServer serv) throws IOException
	{
		server = serv;
		config = serv.getConfiguration();
		connectionRunning = true;
		int port = config.getInt("server-port", 2059);
		LOGGER.info("["+config.getName()+"] Binding to port "+port);
		socket = new ServerSocket(port);
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
					LOGGER.info("["+config.getName()+"] Listening");
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
				catch(Exception e1){e1.printStackTrace();continue;}
			}
		}
	}
}

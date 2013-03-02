package cram.pack.dedicatedserver;

import java.io.DataInputStream;

public class ReadThread
{
	DataInputStream stream = null;
	public ReadThread(DataInputStream dis)
	{
		stream = dis;
	}
	boolean running = true;
	public void run()
	{
		while(running)
		{
			try
			{
				
			}
			catch(Exception e)
			{
				
			}
		}
			
	}
}

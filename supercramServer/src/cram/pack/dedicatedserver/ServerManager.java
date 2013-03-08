package cram.pack.dedicatedserver;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import cram.pack.dedicatedserver.SupercramServer;

public class ServerManager
{
	public static void main(String[] args)
	{
		if(args.length==0)
		{
			System.out.println("Must specify server launchfile: java -jar cramtheserver.jar BasicServer [--nothreading]");
			return;
		}
		List<SupercramServer> servers = new LinkedList<SupercramServer>();
		boolean noThread = true;
		for(int i=0;i<args.length;i++)
		{
			if(args[i].equalsIgnoreCase("--nothreading"))
			{
				noThread = true;
				continue;
			}
			try
			{
				ServerConfigurationManager cfg = new ServerConfigurationManager(args[i], new File(".",args[i]));
				servers.add(new SupercramServer(cfg));
			}
			catch(ServerFailedToStartException e)
			{
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
				System.out.println("Server failed to start!");
				System.out.println("-----------------------");
				System.out.println();
				System.out.println(e.getMessage());
				System.out.println();
				System.out.println("@@@@@@@@@@@@@@@@@@@@@@@");
			}
			catch(Exception e)
			{
				System.out.println("Couldn't load config for "+args[i]);
				e.printStackTrace();
				continue;
			}
		}
		if(servers.isEmpty())
		{
			System.out.println("No servers started!");
			return;
		}
		if(noThread)
		{
			Iterator<SupercramServer> servI = servers.iterator();
			while(servI.hasNext())
			{
				servI.next().start();
				System.out.println("PLAYER ADDED TO SERVER");
			}
			
		}
		threadConsoleForever();
	}
	public static void println(String ln)
	{
		print(ln+"\n");
	}
	private static ConcurrentLinkedQueue<String> printStream = new ConcurrentLinkedQueue<String>();
	public static void print(String s)
	{
		printStream.add(s);
	}
	public static void threadConsoleForever()
	{
		
	}
}

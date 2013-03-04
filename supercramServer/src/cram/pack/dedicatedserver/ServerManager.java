package cram.pack.dedicatedserver;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerManager
{
	public static void main(String[] args)
	{
		if(args.length==0)
		{
			System.out.println("Must specify server launchfile: java -jar cramtheserver.jar BasicServer --nothreading");
			return;
		}
		List<CRAMTheServer> servers = new LinkedList<CRAMTheServer>();
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
				
				ServerConfigurationManager cfg = new ServerConfigurationManager(new File(".",args[i]));
				servers.add(new CRAMTheServer(cfg));
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
		if(!noThread)
		{
			Iterator<CRAMTheServer> servI = servers.iterator();
			while(servI.hasNext())
				servI.next().start();
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

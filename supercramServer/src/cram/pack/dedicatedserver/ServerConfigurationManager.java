package cram.pack.dedicatedserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConfigurationManager
{
	
	public ServerConfigurationManager(File file)
	{
		if(file==null)
			throw new IllegalArgumentException("Server path doesn't exist");
		if(!file.exists())
			file.mkdir();
		if(!file.isDirectory())
			throw new IllegalArgumentException("Server path is not directory");
		File propsFile = new File(file,"server.properties");
		if(!propsFile.exists())
			createPropsFile(propsFile);
		if(!propsFile.exists())
			throw new IllegalArgumentException("Couldn't create props file");
		loadProps(propsFile);
		
		//File loginFile = new File(file,"players.db");
	}
	private void createPropsFile(File propsFile) 
	{
		try
		{
			FileWriter fw = new FileWriter(propsFile);
			fw.write("server-ip:\nserver-port:2059\nserver-name:No name :(\nmax-players:5\n");
			fw.close();
		}
		catch(IOException e){}
	}
	private void loadProps(File propsFile)
	{
		try
		{
			FileReader fr = new FileReader(propsFile);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while(line!=null)
			{
				if(!line.startsWith("#") && line.length()>2 && line.contains(":"))
				{
					String[] lineParts = line.split(":");
					properties.put(lineParts[0], lineParts[1]);
				}
				line = br.readLine();
			}
			br.close();
		}
		catch(Exception e){}
	}
	private ConcurrentHashMap<String,Object> properties = new ConcurrentHashMap<String,Object>();
	private ConcurrentHashMap<String,String> logins = new ConcurrentHashMap<String,String>();
	public String toString(String key)
	{
		Object oj = properties.get(key);
		if(oj==null)
			return null;
		return oj.toString();
	}
	public String getString(String key, String defaultValue)
	{
		Object oj = properties.get(key);
		if(oj==null || !(oj instanceof String))
			return defaultValue;
		return (String)oj;
	}
	public boolean getBoolean(String key, boolean defaultValue)
	{
		Object oj = properties.get(key);
		if(oj==null || !(oj instanceof Boolean))
			return defaultValue;
		return (Boolean)oj;
	}
	public int getInt(String key, int defaultValue)
	{
		Object oj = properties.get(key);
		if(oj==null || !(oj instanceof Integer))
			return defaultValue;
		return (Integer)oj;
	}
	public long getLong(String key, long defaultValue)
	{
		Object oj = properties.get(key);
		if(oj==null || !(oj instanceof Long))
			return defaultValue;
		return (Long)oj;
	}
	public double getDouble(String key, double defaultValue)
	{
		Object oj = properties.get(key);
		if(oj==null || !(oj instanceof Double))
			return defaultValue;
		return (Double)oj;
	}
	public float getFloat(String key, float defaultValue)
	{
		Object oj = properties.get(key);
		if(oj==null || !(oj instanceof Integer))
			return defaultValue;
		return (Float)oj;
	}
	public byte checkLogin(String username, String password)
	{
		String realPassword = logins.get(username);
		if(realPassword==null)
			return 1;
		if(realPassword.equalsIgnoreCase(password))
			return 2;
		return 3;
		
	}
	
}

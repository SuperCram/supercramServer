package cram.pack.dedicatedserver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ServerConfigurationManager
{
	
	public ServerConfigurationManager(File file)
	{
		if(file==null || !file.exists())
			throw new IllegalArgumentException("Server path doesn't exist");
		if(!file.isDirectory())
			throw new IllegalArgumentException("Server path is not directory");
		File propsFile = new File(file,"server.properties");
		File loginFile = new File(file,"players.db");
		if(!propsFile.exists())
			createPropsFile(propsFile);
		
	}
	private void createPropsFile(File propsFile) 
	{
		try
		{
			FileWriter fw = new FileWriter(propsFile);
			fw.write("server-ip:");
			fw.write("server-port:2059");
			fw.write("server-name:No name :(");
			fw.write("max-players:5");
			fw.close();
		}
		catch(IOException e)
		{
			
		}
		
	}
	private ConcurrentHashMap<String,Object> properties = new ConcurrentHashMap<String,Object>();
	private ConcurrentHashMap<String,Integer> logins = new ConcurrentHashMap<String,Integer>();
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
		Integer b = logins.get(username+password);
		if(b==null)
			return 3;
		else
			return b.byteValue();
	}
	
}

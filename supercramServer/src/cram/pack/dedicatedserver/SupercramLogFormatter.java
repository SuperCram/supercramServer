package cram.pack.dedicatedserver;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SupercramLogFormatter extends Formatter
{

	@Override
	public String format(LogRecord record)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("APLE:"+record.getMessage());
		return sb.toString();
	}
	
}

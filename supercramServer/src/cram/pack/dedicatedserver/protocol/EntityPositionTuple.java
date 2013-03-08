package cram.pack.dedicatedserver.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class EntityPositionTuple
{
	@Override
	public String toString() {
		return "EntityPositionTuple(contents="+Integer.toBinaryString(contents)+",x="+x+",y="+y+",motX="+motX+",motY="+motY+")";
	}
	int x;
	int y;
	double motX;
	double motY;
	byte contents = 0;
	public EntityPositionTuple setX(int x)
	{
		this.x=x;
		contents |= 1;
		return this;
	}
	public EntityPositionTuple setY(int y)
	{
		this.y=y;
		contents |= 2;
		return this;
	}
	public EntityPositionTuple setMotX(double motX)
	{
		this.motX=motX;
		contents |= 4;
		return this;
	}
	public EntityPositionTuple setMotY(double motY)
	{
		this.motY=motY;
		contents |= 8;
		return this;
	}
	public EntityPositionTuple setOnGround(boolean b)
	{
		contents |= 16;
		if(!b)
			contents ^= 16;
		return this;
	}
	public EntityPositionTuple setHasMeta(boolean b)
	{
		contents |= 32;
		if(!b)
			contents ^= 32;
		return this;
	}
	public EntityPositionTuple(DataInputStream dis) throws IOException
	{
		contents = dis.readByte();
		if((contents&1)==1)
			x = dis.readInt();
		if((contents&2)==2)
			y = dis.readInt();
		if((contents&4)==4)
			motX = dis.readFloat();
		if((contents&8)==8)
			motY = dis.readFloat();
	}
	public boolean getOnGround()
	{
		return ((contents&16)==16);
	}
	public boolean hasMeta()
	{
		return ((contents&32)==32);
	}
	public EntityPositionTuple(){}
	public void write(DataOutputStream dos) throws IOException
	{
		dos.writeByte(contents);
		if((contents&1)==1)
			dos.writeInt(x);
		if((contents&2)==2)
			dos.writeInt(y);
		if((contents&4)==4)
			dos.writeFloat((float)motX);
		if((contents&8)==8)
			dos.writeFloat((float)motY);
	}
}

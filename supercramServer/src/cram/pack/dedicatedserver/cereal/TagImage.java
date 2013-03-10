package cram.pack.dedicatedserver.cereal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class TagImage extends Tag
{
	public TagImage() {
		id = 8;
	}
	BufferedImage image;
	@Override
	public void read(DataInputStream dis) throws IOException {
		int width = dis.readInt();
		int height = dis.readInt();
		image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
		GZIPInputStream gis = new GZIPInputStream(dis);
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<width;y++)
			{
				int i = gis.read();
				int j = gis.read();
				int k = gis.read();
				int l = gis.read();
				if ((i | j | k | l) < 0)
					throw new EOFException();
				image.setRGB(x, y, ((i<<24)+(j<<16)+(k<<8)+l));
			}
		}
	}
	@Override
	public void write(DataOutputStream dos) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		dos.writeInt(width);
		dos.writeInt(height);
		GZIPOutputStream gos = new GZIPOutputStream(dos);
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<width;y++)
			{
				int i = image.getRGB(x, y);
				gos.write(i>>>24&0xFF);
				gos.write(i>>>16&0xFF);
				gos.write(i>>>8&0xFF);
				gos.write(i&0xFF);
			}
		}
	}
	public Image get() {
		return image;
	}
}

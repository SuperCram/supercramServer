package cram.pack.dedicatedserver;

import java.util.Iterator;
import java.util.LinkedList;

import cram.pack.dedicatedserver.protocol.EntityPositionTuple;
import cram.pack.dedicatedserver.protocol.Packet20EntitySpawned;
import cram.pack.dedicatedserver.protocol.Packet22EntityDestory;
import cram.pack.dedicatedserver.protocol.Packet23EntityMove;

public class NetEntityTracker
{
	class LastKnown{
		int x=-54321,y=-54321,flush=-1;
		double motX=Integer.MIN_VALUE,motY=Integer.MIN_VALUE;
		boolean onGround;
		Entity ent;
		public EntityPositionTuple createDeltaEpm()
		{
			EntityPositionTuple ept = null;
			flush--;
			if(flush<=0)
			{
				flush = flushTicks;
				if(ent.aabb.x!=x)
				{
					if(ept==null)
						ept = new EntityPositionTuple();
					x = ent.aabb.x;
					ept.setX(x);
				}
				if(ent.aabb.y!=y)
				{
					if(ept==null)
						ept = new EntityPositionTuple();
					y = ent.aabb.y;
					ept.setY(y);
					
				}
				if(ent.motX!=motX)
				{
					if(ept==null)
						ept = new EntityPositionTuple();
					motX = ent.motX;
					ept.setMotX(motX);
					
				}
				if(ent.motY!=motY)
				{
					if(ept==null)
						ept = new EntityPositionTuple();
					motY = ent.motY;
					ept.setMotY(motY);
				}
			}
			else
			{
				if(ent.motX!=motX)
				{
					if(ent.aabb.x!=x)
					{
						if(ept==null)
							ept = new EntityPositionTuple();
						x = ent.aabb.x;
						ept.setX(x);
					}
					if(ept==null)
						ept = new EntityPositionTuple();
					motX = ent.motX;
					ept.setMotX(motX);
				}
				if(ent.motY!=motY)
				{
					if(ent.aabb.y!=y)
					{
						if(ept==null)
							ept = new EntityPositionTuple();
						ept.setY(y);
						y = ent.aabb.y;
					}
					if(ept==null)
						ept = new EntityPositionTuple();
					motY = ent.motY;
					ept.setMotY(motY);
				}
			}
			if(ept!=null)
				ept.setOnGround(ent.onGround);
			return ept;
		}
	}
	private int flushTicks = 10;
	LinkedList<LastKnown> known = new LinkedList<LastKnown>();
	private NetServerHandler nsh;
	public NetEntityTracker(NetServerHandler nsh)
	{
		this.nsh = nsh;
	}
	public void update()
	{
		Iterator<LastKnown> it = known.iterator();
		while(it.hasNext())
		{
			LastKnown lk = it.next();
			if(lk.ent.deleted)
			{
				nsh.sendPacket(new Packet22EntityDestory(lk.ent.UID));
				it.remove();
			}
			else
			{
				EntityPositionTuple ept = lk.createDeltaEpm();
				if(ept!=null)
					nsh.sendPacket(new Packet23EntityMove(lk.ent.UID, ept));
			}
		}
	}
	public void addEntity(Entity ent)
	{
		LastKnown lk = new LastKnown();
		lk.ent = ent;
		nsh.sendPacket(new Packet20EntitySpawned(ent.UID, ent.type, lk.createDeltaEpm()));
		known.add(lk);
	}
	public void clearEntities()
	{
		known.clear();
	}
}

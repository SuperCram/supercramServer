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
		float motX=Float.MIN_VALUE,motY=Float.MIN_VALUE;
		boolean onGround;
		Entity ent;
		public EntityPositionTuple createFullEPT()
		{
			EntityPositionTuple ept = new EntityPositionTuple();
			ept.setX(ent.aabb.x);
			ept.setY(ent.aabb.y);
			ept.setMotX(ent.motX);
			ept.setMotY(ent.motY);
			ept.setOnGround(ent.onGround);
			ept.setFacingRight(ent.facingRight);
			
			x = ent.aabb.x;
			y = ent.aabb.y;
			motX = ent.motX;
			motY = ent.motY;
			return ept;
		}
		public EntityPositionTuple createDeltaEpm()
		{
			flush--;
			if(flush<=0)
			{
				flush = flushTicks;
				return createFullEPT();
			}
			EntityPositionTuple ept = null;
			if(ent.forceUpdateMotX)
			{
				if(ept==null)
					ept = new EntityPositionTuple();
				motX = ent.motX;
				ept.setMotX(ent.motX);
			}
			if(ent.forceUpdateMotY)
			{
				if(ept==null)
					ept = new EntityPositionTuple();
				motY = ent.motY;
				ept.setMotY(ent.motY);
			}
			if(ent.forceUpdateX)
			{
				if(ept==null)
					ept = new EntityPositionTuple();
				x = ent.aabb.x;
				ept.setX(ent.aabb.x);
			}
			if(ent.forceUpdateY)
			{
				if(ept==null)
					ept = new EntityPositionTuple();
				y = ent.aabb.y;
				ept.setY(ent.aabb.y);
			}
			if(ept!=null)
			{
				ept.setOnGround(ent.onGround);
				ept.setFacingRight(ent.facingRight);
			}
			return ept;
		}
	}
	private int flushTicks = 20;
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

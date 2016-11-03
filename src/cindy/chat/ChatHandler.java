package cindy.chat;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.cindy.Buffer;
import net.sf.cindy.Packet;
import net.sf.cindy.PacketDecoder;
import net.sf.cindy.PacketEncoder;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.packet.DefaultPacket;
import net.sf.cindy.util.Charset;

public class ChatHandler extends SessionHandlerAdapter {

	private static final Set sessions = Collections.synchronizedSet(new HashSet());
	
	private static final Object USER_ID_ATTR = "userId";
	
	private static int counter = 0;
	
	public void sessionStarted(Session session)throws Exception{
		sessions.add(session);
		int id = ++counter;
		session.setAttribute(USER_ID_ATTR, new Integer(id));
		session.send("Welcome, User " +id +"!");
		send(session, "User " +id + " log in");
	}
	
	public void sessionTimeout(Session session){
		session.close();
	}
	
	public void sessionClose(Session session)throws Exception{
		sessions.remove(session);
		send(session, "User " +session.getAttribute(USER_ID_ATTR) +" log out");
	}
	
	public void objectReceived(Session session, Object obj)throws Exception{
		String senderId = session.getAttribute(USER_ID_ATTR).toString();
		String msg = (String)obj;
		send(session, "User " +senderId +" say: " +msg);
	}
	
	private void send(Session srcSession, String message){
		//System.out.println(message);
		synchronized(sessions){
			for(Iterator it = sessions.iterator(); it.hasNext();){
				Session session = (Session)it.next();
				if(session != srcSession){
					// when client send other message to server, client can real receive this sent message
					session.send(message);
				}
			}
		}
	}
	
	private static final byte[] TOKEN = System.getProperty("line.separator").getBytes();
	
	static class ChatMessageDecoder implements PacketDecoder{
		public Object decode(Session session, Packet packet)throws Exception{
			Buffer buffer = packet.getContent();
			int index = buffer.indexOf(TOKEN);
			if(index >0){
				String s = buffer.getString(Charset.SYSTEM, index-buffer.position());
				buffer.skip(TOKEN.length);
				return s;
			}
			return null;
		}
	}
	
	static class ChatMessageEncoder implements PacketEncoder{
		public Packet encode(Session session, Object obj)throws Exception{
			String s = (String)obj;
			ByteBuffer buffer = Charset.SYSTEM.encode(s);
			return new DefaultPacket(BufferFactory.allocate(buffer.remaining() 
					+TOKEN.length).put(buffer).put(TOKEN).flip());
		}
	}

}

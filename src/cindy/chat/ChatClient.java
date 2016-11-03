package cindy.chat;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.sf.cindy.Session;
import net.sf.cindy.SessionType;
import net.sf.cindy.encoder.BufferEncoder;
import net.sf.cindy.session.SessionFactory;


public class ChatClient {
	
	public static void send(String ip, int port){
		SocketAddress address = new InetSocketAddress(ip, port);
		System.out.println("Chart client start at " +address);
		
		Session session = SessionFactory.createSession(SessionType.TCP);
		session.setRemoteAddress(address);
		session.setPacketEncoder(new BufferEncoder());
		session.setSessionHandler(new ChatClientHandler());
		session.start();
	}

}

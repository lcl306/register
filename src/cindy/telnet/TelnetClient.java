package cindy.telnet;

import java.io.IOException;
import java.net.InetSocketAddress;

import net.sf.cindy.Buffer;
import net.sf.cindy.Packet;
import net.sf.cindy.PacketDecoder;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.SessionType;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.encoder.BufferEncoder;
import net.sf.cindy.session.SessionFactory;
import net.sf.cindy.util.Charset;
import cindy.ServerInfo;

public class TelnetClient {
	
	private static final int BUFFER_SIZE = 4096;
	
	private static Session startSession(boolean tcp, String host, int port){
		System.out.println("start telnet using " +(tcp?"tcp":"udp"));
		Session session = SessionFactory.createSession(tcp?SessionType.TCP:SessionType.UDP);
		session.setRemoteAddress(new InetSocketAddress(host, port));
		
		session.setPacketEncoder(new BufferEncoder());
		session.setPacketDecoder(new PacketDecoder(){
			public Object decode(Session session, Packet packet)throws Exception{
				Buffer content = packet.getContent();
				return content.getString(Charset.SYSTEM, content.remaining());
			}
		});
		session.setSessionHandler(new SessionHandlerAdapter(){
			public void objectReceived(Session session, Object obj)throws Exception{
				System.out.println(obj);
			}
		});
		session.start().complete();
		return session;
	}
	
	private static boolean send(Session session, byte[] data, int pos){
		return session.send(BufferFactory.wrap(data, 0, pos)).complete();
	}
	
	public static void start(String type, String host)throws IOException{
		boolean tcp = "tcp".equalsIgnoreCase(type);
		int port = ServerInfo.TELNET_SERVER_PORT;		
		Session session = startSession(tcp, host, port);
		byte[] data = new byte[BUFFER_SIZE];
		while(true){
			int count = System.in.read(data);
			if(count<0) break;
			if(!send(session, data, count)){
				break;
			}
		}
	}

}

package cindy.file;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import net.sf.cindy.Session;
import net.sf.cindy.SessionType;
import net.sf.cindy.encoder.BufferEncoder;
import net.sf.cindy.session.SessionFactory;
import cindy.ServerInfo;

public class FileClient {
	
	public static void send(String filename, String ip){
		File file = new File(filename);
		if(!file.exists()){
			System.err.println(file +" not found");
			return;
		}
		SocketAddress address = new InetSocketAddress(ip, ServerInfo.FILE_SERVER_PORT);
		System.out.println("start transfer " +file +" to " +address);
		
		Session session = SessionFactory.createSession(SessionType.TCP);
		session.setRemoteAddress(address);
		session.setPacketEncoder(new BufferEncoder());
		session.setSessionHandler(new FileClientSessionHandler(file));
		session.start();
	}

}

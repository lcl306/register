package cindy.file;

import net.sf.cindy.Session;
import net.sf.cindy.SessionAcceptor;
import net.sf.cindy.SessionAcceptorHandler;
import net.sf.cindy.SessionType;
import net.sf.cindy.session.SessionFactory;
import cindy.ServerInfo;
import cindy.file.FileServerSessionHandler.FileTransferMessageDecoder;

public class FileServer {
	
	public static void start(){
		SessionAcceptor acceptor = SessionFactory.createSessionAcceptor(SessionType.TCP);
		acceptor.setListenPort(ServerInfo.FILE_SERVER_PORT);
		// SessionAcceptorHandler belongs to server
		acceptor.setAcceptorHandler(new SessionAcceptorHandler(){
			public void sessionAccepted(SessionAcceptor acceptor, Session session) throws Exception {
				session.setPacketDecoder(new FileTransferMessageDecoder());
				// SessionHandler belongs to session
				session.setSessionHandler(new FileServerSessionHandler());
				session.start();
			}
			public void exceptionCaught(SessionAcceptor acceptor, Throwable cause) {
				cause.printStackTrace();
			}
		});
		acceptor.start();
		if(acceptor.isStarted()){
			System.out.println("FileServer listen on " +acceptor.getListenAddress());
		}
	}
	
	public static void main(String[] args){
		start();
	}

}

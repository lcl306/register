package cindy.simple;

import net.sf.cindy.Session;
import net.sf.cindy.SessionAcceptor;
import net.sf.cindy.SessionAcceptorHandlerAdapter;
import net.sf.cindy.SessionHandler;
import net.sf.cindy.SessionType;
import net.sf.cindy.session.SessionFactory;
import cindy.ServerInfo;

public class SimpleServer {
	
	private static void startService(int port, final SessionHandler handler, String serviceType){
		SessionAcceptor acceptor = SessionFactory.createSessionAcceptor(SessionType.TCP);
		acceptor.setListenPort(port);
		acceptor.setAcceptorHandler(new SessionAcceptorHandlerAdapter(){
			public void sessionAccepted(SessionAcceptor acceptor, Session session)throws Exception{
				session.setSessionHandler(handler);
				session.start();
			}
		});
		acceptor.start();
		if(acceptor.isStarted()){
			System.out.println(serviceType +" listen on " +acceptor.getListenAddress());
		}
	}
	
	public static void start(){
		startService(ServerInfo.CHARGEN_PORT, new ChargenHandler(), "Chargen service");
		startService(ServerInfo.DAYTIME_PORT, new DaytimeHandler(), "Daytime service");
		startService(ServerInfo.DISCARD_PORT, new DiscardHandler(), "Discard service");
		startService(ServerInfo.ECHO_PORT, new EchoHandler(), "Echo service");
	}
	
	public static void main(String[] args){
		start();
	}

}

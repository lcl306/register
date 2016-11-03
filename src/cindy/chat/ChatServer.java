package cindy.chat;

import net.sf.cindy.Session;
import net.sf.cindy.SessionAcceptor;
import net.sf.cindy.SessionAcceptorHandler;
import net.sf.cindy.SessionType;
import net.sf.cindy.session.SessionFactory;
import cindy.ServerInfo;
import cindy.chat.ChatHandler.ChatMessageDecoder;
import cindy.chat.ChatHandler.ChatMessageEncoder;

public class ChatServer {
	
	public static void main(String[] args) {
		SessionAcceptor acceptor = SessionFactory.createSessionAcceptor(SessionType.TCP);
		acceptor.setListenPort(ServerInfo.CHAT_SERVER_PORT);
		acceptor.setAcceptorHandler(new SessionAcceptorHandler(){
			public void sessionAccepted(SessionAcceptor acceptor, Session session)throws Exception{
				session.setPacketDecoder(new ChatMessageDecoder());
				session.setPacketEncoder(new ChatMessageEncoder());
				session.addSessionFilter(new ChatLogFilter());
				session.setSessionHandler(new ChatHandler());
				session.start();
			}
			public void exceptionCaught(SessionAcceptor acceptor, Throwable cause){
				System.err.println(cause);
			}
		});
		acceptor.start();
		if(acceptor.isStarted())
			System.out.println("ChatSever listen on " +acceptor.getListenAddress());
	}

}

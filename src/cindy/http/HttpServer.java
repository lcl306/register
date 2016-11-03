package cindy.http;

import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import net.sf.cindy.PacketDecoder;
import net.sf.cindy.PacketEncoder;
import net.sf.cindy.Session;
import net.sf.cindy.SessionAcceptor;
import net.sf.cindy.SessionAcceptorHandler;
import net.sf.cindy.SessionHandler;
import net.sf.cindy.SessionType;
import net.sf.cindy.filter.SSLFilter;
import net.sf.cindy.session.SessionFactory;
import cindy.ServerInfo;
import cindy.http.coder.HttpRequestDecoder;
import cindy.http.coder.HttpResponseEncoder;
import cindy.http.handler.CachedFileHandler;
import cindy.http.handler.EchoHandler;

public class HttpServer {
	
	private static final int port = ServerInfo.HTTP_SERVER_PORT;
	private static final PacketEncoder encoder = new HttpResponseEncoder();
	private static final PacketDecoder decoder = new HttpRequestDecoder();
	
	private static boolean isEcho;
	private static SSLContext sslc;
	private static SessionHandler handler;
	
	public static void main(String[] args)throws Exception{
		//client    http://localhost:8010/param
		//start("-echo", "-secure");
		//clinet    https://localhost:8010/param
		start("", "");
	}
	
	protected static void start(String echo, String secure)throws Exception{
		isEcho = echo.equalsIgnoreCase("-echo");
		handler = isEcho?(SessionHandler)new EchoHandler():(SessionHandler)new CachedFileHandler();
		sslc = secure.equalsIgnoreCase("-secure")?getSSLcontext():null;
		
		SessionAcceptor acceptor = SessionFactory.createSessionAcceptor(SessionType.TCP);
		acceptor.setListenPort(port);
		acceptor.setAcceptorHandler(new SessionAcceptorHandler(){
			public void exceptionCaught(SessionAcceptor acceptor, Throwable cause){
				System.err.println(cause);
			}
			public void sessionAccepted(SessionAcceptor acceptor, Session session)throws Exception{
				startSession(session);
			}
		});
		acceptor.start();
		if(acceptor.isStarted()) print();
	}
	
	private static void startSession(Session session){
		session.setPacketDecoder(decoder);
		session.setPacketEncoder(encoder);
		session.setSessionHandler(handler);
		if(sslc!=null){
			SSLFilter filter = new SSLFilter(sslc);
			filter.setClientMode(false);
			session.addSessionFilter(filter);
		}
		session.start();
	}
	
	private static SSLContext getSSLcontext()throws Exception{
		char[] password = "password".toCharArray();
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(HttpServer.class.getClassLoader().getResourceAsStream("com/learning/cindy/http/testkeys"),password);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ks, password);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ks);
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		return context;
	}
	
	private static void print() {
        System.out.println((isEcho ? "echo" : "file")
                + " http server listen on port " + port + " with ssl "
                + (sslc == null ? "disabled" : "enabled"));
    }

}

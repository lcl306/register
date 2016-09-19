package minas.test;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class WebsocketServerHanlder extends IoHandlerAdapter {
	
public static Logger logger = Logger.getLogger(WebsocketServerHanlder.class);
	
	@Override
	public void messageReceived(IoSession session, Object message)throws Exception{
		String get = (String)message;
		logger.info("sever receive data: " +get +new Date());
		//WebSocketHandShakeResponse response = new WebSocketHandShakeResponse(new Date().toString());
		session.write(new Date());
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception{
		//WebSocketHandShakeResponse get = (WebSocketHandShakeResponse)message;
		//logger.info("server send receive data: " +get.getResponse());
		/*WebSocketHandShakeResponse response = new WebSocketHandShakeResponse(new Date().toString());
		session.write(response);
		session.getCloseFuture().awaitUninterruptibly();*/
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)throws Exception{
		logger.error(" error!", cause);
	}

}

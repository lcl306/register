package minas.test;

import java.util.Date;

import minas.mina.filter.websocket.WebSocketHandShakeResponse;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class WebsocketClientHanlder extends IoHandlerAdapter {
	
	public static Logger logger = Logger.getLogger(WebsocketClientHanlder.class);
	
	@Override
	public void messageReceived(IoSession session, Object message)throws Exception{
		logger.info("client receive data: " +message +new Date());
		if(message instanceof WebSocketHandShakeResponse){
			WebSocketHandShakeResponse response = (WebSocketHandShakeResponse)message;
			String res = response.getResponse();
			logger.info("client receive data: " +res);
		}
	}
	
	@Override
	public void messageSent(IoSession session, Object message) throws Exception{
		logger.info("client send receive data: " +message);
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)throws Exception{
		logger.error(" error!", cause);
	}

}

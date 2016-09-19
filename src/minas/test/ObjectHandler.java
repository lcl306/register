package minas.test;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ObjectHandler extends IoHandlerAdapter{
	
	public static Logger logger = Logger.getLogger(ObjectHandler.class);
	
	private String type = "";
	
	public ObjectHandler(String type){
		this.type = type;
	}
	
	@Override
	public void messageReceived(IoSession session, Object message)throws Exception{
		PhoneDto dto = (PhoneDto)message;
		logger.info(type+" receive data: " +dto.getSendCode()+"_"+dto.getReceiveCode()+"_"+dto.getMessage());
		if("server".equals(type)){
			session.write(dto);
		}
	}
	
	@Override
	public void messageSent(IoSession session, Object messge) throws Exception{
		if("server".equals(type)){
			session.close(true);
		}
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)throws Exception{
		logger.error(type+" error!", cause);
	}

}

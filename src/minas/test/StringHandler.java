package minas.test;

import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class StringHandler extends IoHandlerAdapter{
	
	public static Logger logger = Logger.getLogger(StringHandler.class);
	
	private String type = "";
	
	public StringHandler(String type){
		this.type = type;
	}
	
	/*@Override
	public void sessionCreated(IoSession session) throws Exception{
		logger.info(type+" session created");
	}*/
	
	/*@Override
	public void sessionOpened(IoSession session) throws Exception{
		logger.info(type+" session opened");
	}*/
	
	@Override
	public void messageReceived(IoSession session, Object message)throws Exception{
		String msg = message.toString();
		logger.info(type+" receive data: " +msg);
		if("server".equals(type)){
			if("bye".equals(msg)){
				session.close(true);
			}
			if("nodeData".equals(msg)){
				while(true){
					session.write(new Date());
					Thread.sleep(2000);
				}
			}
			session.write("已经连接..." +new Date());
		}
	}
	
	@Override
	public void messageSent(IoSession session, Object messge) throws Exception{
		/*if("server".equals(type)){
			session.close(true);  // session.close()，由服务器主动关闭连接，变成短连接；否则连接不关闭，是长连接
		}*/
		//定时发送数据
	}
	
	/*@Override
	public void sessionClosed(IoSession session) throws Exception{
		logger.info(type +" session closed");
	}*/
	
	/*@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception{
		logger.info(type +" session idled");
	}*/
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)throws Exception{
		logger.error(type +" error!", cause);
	}
}

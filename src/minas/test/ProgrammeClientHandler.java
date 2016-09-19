package minas.test;

import minas.mina.filter.myprotocol.ProgrammeDto;
import minas.mina.filter.myprotocol.ResponseMessage;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ProgrammeClientHandler extends IoHandlerAdapter {
	
	private static Logger logger = Logger.getLogger(ProgrammeClientHandler.class);
	
	@Override
	public void messageReceived(IoSession session, Object message)throws Exception{
		if(message instanceof ResponseMessage){
			ResponseMessage res = (ResponseMessage)message;
			String channelName = res.getChannelName();
			ProgrammeDto[] programmes = res.getProgrammes();
			logger.info("客户端收到的消息为：channel_name=" +channelName);
			if(programmes!=null && programmes.length>0){
				for(int i=0; i<programmes.length; i++){
					ProgrammeDto pm = programmes[i];
					logger.info("客户端接收到的消息为：BeginTime="+pm.getBeginTime());
					logger.info("客户端接收到的消息为：DayIndex="+pm.getDayIndex());
					logger.info("客户端接收到的消息为：EventName="+pm.getProgrammeName());
					logger.info("客户端接收到的消息为：Status="+pm.getStatus());
					logger.info("客户端接收到的消息为：TotalTime="+pm.getTotalTime());
					logger.info("客户端接收到的消息为：url="+pm.getUrl());
				}
			}
		}else{
			logger.info("未知类型");
		}
	}

}

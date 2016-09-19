package minas.test;

import minas.mina.filter.myprotocol.ProgrammeDto;
import minas.mina.filter.myprotocol.RequestMessage;
import minas.mina.filter.myprotocol.ResponseMessage;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class ProgrammeServerHandler extends IoHandlerAdapter {
	
	public static Logger logger = Logger.getLogger(ProgrammeServerHandler.class);
	
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception{
		if(message instanceof RequestMessage){
			RequestMessage req = (RequestMessage)message;
			int channelId = req.getChannelId();
			String channelDesc = req.getChannelDesc();
			logger.info("服务器接收到的数据是：channel_id="+channelId +"  channel_desc"+channelDesc);
			ResponseMessage res = new ResponseMessage();
			res.setChannelName("CCTV1高清频道");
			ProgrammeDto[] programmes = new ProgrammeDto[2];
			for(int i=0; i<programmes.length; i++){
				ProgrammeDto pm = new ProgrammeDto();
				pm.setBeginTime(10);
				pm.setDayIndex(1);
				pm.setProgrammeName("风云第一的"+i);
				pm.setStatus(1);
				pm.setTotalTime(100+i);
				pm.setUrl("www.baidu.com");
				programmes[i] = pm;
			}
			res.setProgrammes(programmes);
			session.write(res);
		}else{
			logger.info("未知请求");
		}
	}
	
	
	
	@Override
	public void messageSent(IoSession session, Object message)throws Exception{
		session.close(true);
		logger.info("服务端发送信息成功...");
	}

}

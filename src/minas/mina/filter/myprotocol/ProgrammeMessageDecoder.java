package mina.filter.myprotocol;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class ProgrammeMessageDecoder implements MessageDecoder {
	
	private Logger logger = Logger.getLogger(ProgrammeMessageDecoder.class);
	
	private Charset charset;
	
	public ProgrammeMessageDecoder(Charset charset){
		this.charset = charset;
	}

	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		//报头长度==6（2+4）
		if(in.remaining()<6){
			return MessageDecoderResult.NEED_DATA;
		}
		// tag正常
		short tag = in.getShort();
		if(tag==(short)0x0001 || tag==(short)0x8001){ //将16进制的标识值转换为10进制的short类型
			logger.info("请求标识符：" +tag);
		}else{
			logger.error("未知的解码类型...");
			return MessageDecoderResult.NOT_OK;
		}
		// 真实数据长度
		int len = in.getInt();
		if(in.remaining()<len){
			return MessageDecoderResult.NEED_DATA;
		}
		return MessageDecoderResult.OK;
	}

	public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		logger.info("解码："+in.toString());
		CharsetDecoder decoder = charset.newDecoder();
		AbsMessage message = null;
		short tag = in.getShort();
		int len = in.getInt();
		byte[] temp = new byte[len];
		in.get(temp);  //将in的数据放入temp
		
		// 基本数据区
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.put(temp);  //将temp数据放入buf
		buf.flip();
		
		//真实数据区
		IoBuffer dataBuf = IoBuffer.allocate(100).setAutoExpand(true);
		dataBuf.putShort(tag);
		dataBuf.putInt(len);
		dataBuf.put(temp); //将temp数据放入dataBuf
		dataBuf.flip();
		
		if(tag==(short)0x0001){  //服务端解码
			RequestMessage req = new RequestMessage();
			short channelId = buf.getShort();
			byte channelDescLen = buf.get();
			String channelDesc = null;
			if(channelDescLen>0){
				channelDesc = buf.getString(channelDescLen, decoder);
			}
			req.setChannelId(channelId);
			req.setChannelDesc(channelDesc);
			message = req;
		}else if(tag==(short)0x8001){  //客户端解码
			ResponseMessage res = new ResponseMessage();
			int channelAddr = buf.getInt();
			byte channelLen = buf.get();
			if(dataBuf.position()==0){
				dataBuf.position(channelAddr);  //定位到数据的地址
			}
			String channelName = null;
			if(channelLen>0){
				channelName = dataBuf.getString(channelLen, decoder);
			}
			res.setChannelName(channelName);
			short programmeNum = buf.getShort();
			ProgrammeDto[] programmes = new ProgrammeDto[programmeNum];
			for(int i=0; i<programmeNum; i++){
				ProgrammeDto pm = new ProgrammeDto();
				byte dayIndex = buf.get();
				buf.getInt();
				byte programmeNameLen = buf.get();
				String programmeName = null;
				if(programmeNameLen>0){
					programmeName = dataBuf.getString(programmeNameLen, decoder);
				}
				int beginTime = buf.getInt();
				short totalTime = buf.getShort();
				byte status = buf.get();
				buf.getInt();
				byte urlLen = buf.get();
				String url = null;
				if(urlLen>0){
					url = dataBuf.getString(urlLen, decoder);
				}
				pm.setDayIndex(dayIndex);
				pm.setProgrammeName(programmeName);
				pm.setBeginTime(beginTime);
				pm.setTotalTime(totalTime);
				pm.setStatus(status);
				pm.setUrl(url);
				programmes[i] = pm;
			}
			res.setProgrammes(programmes);
			message = res;
		}else{
			logger.error("未找到解码器...");
		}
		out.write(message);
		return MessageDecoderResult.OK;
	}

	public void finishDecode(IoSession session, ProtocolDecoderOutput out) throws Exception {
		
	}

}

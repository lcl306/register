package mina.filter.myprotocol;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

public class ProgrammeMessageEncoder implements MessageEncoder<AbsMessage> {

	private Logger logger = Logger.getLogger(ProgrammeMessageEncoder.class);
	
	private Charset charset;
	
	public ProgrammeMessageEncoder(Charset charset){
		this.charset = charset;
	}
	
	public void encode(IoSession session, AbsMessage message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(message.getTag());
		buf.putInt(message.getLen(charset));
		if(message instanceof RequestMessage){
			RequestMessage req = (RequestMessage)message;
			buf.putShort((short)req.getChannelId());
			buf.put((byte)req.getChannelDesc().getBytes(charset).length);
			buf.putString(req.getChannelDesc(), charset.newEncoder());
		}else if(message instanceof ResponseMessage){
			ResponseMessage res = (ResponseMessage)message;
			CharsetEncoder encoder = charset.newEncoder();
			IoBuffer dataBuf = IoBuffer.allocate(100).setAutoExpand(true);
			int offset = res.getDataOffset();
			buf.putInt(offset); //channel_addr 真实地址
			byte channelNameLen = 0;
			if(res.getChannelName()!=null){
				channelNameLen = (byte)res.getChannelName().getBytes(charset).length;
			}
			buf.put(channelNameLen);
			if(channelNameLen>0){
				dataBuf.putString(res.getChannelName(), encoder);
				offset += channelNameLen;
			}
			ProgrammeDto[] programmes = res.getProgrammes();
			if(programmes!=null){
				buf.putShort((short)programmes.length);
				for(ProgrammeDto pm : programmes){
					buf.put((byte)pm.getDayIndex());
					buf.putInt(offset);
					String programmeName = pm.getProgrammeName();
					byte programmeNameLen = 0;
					if(programmeName!=null){
						programmeNameLen = (byte)programmeName.getBytes(charset).length;
					}
					buf.put(programmeNameLen);
					if(programmeNameLen>0){
						dataBuf.putString(programmeName, encoder);
						offset += programmeNameLen;
					}
					buf.putInt(pm.getBeginTime());
					buf.putShort((short)pm.getTotalTime());
					buf.put((byte)pm.getStatus());
					buf.putInt(offset);
					String url = pm.getUrl();
					byte urlLen = 0;
					if(url!=null){
						urlLen = (byte)url.getBytes(charset).length;
					}
					buf.put(urlLen);
					if(urlLen>0){
						dataBuf.putString(url, encoder);
						offset += urlLen;
					}
				}
			}
			if(dataBuf.position()>0){
				buf.put(dataBuf.flip());
			}
		}
		buf.flip();
		logger.info("编码" +buf.toString());
		out.write(buf);
	}

}

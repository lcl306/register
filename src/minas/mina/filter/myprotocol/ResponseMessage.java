package minas.mina.filter.myprotocol;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

/**
 *            偏移地址(DataOffset)
 * ........................................................................................................................................................................................................
 *  Tag    数据区length     基本数据区                                                                                                                                                                                                                                                                                                                       真实数据区
 *  0x8001                channel_addr(4B) channel_len(B) programme_count(2B) for(programeCount){day_index(B) event_addr(4B) event_len(B) start_time(4B) total_time(2B) status(B) url_addr(4B) url_len(B)}
 * --2B---|----4B------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------
 *                      ............................................................................................................................................................................................................
 *                                    数据区len
 * */
public class ResponseMessage extends AbsMessage {
	
	private ProgrammeDto[] programmes;
	
	private String channelName;
	
	private Logger logger = Logger.getLogger(ResponseMessage.class);

	@Override
	public short getTag() {
		return (short)0x8001;
	}

	@Override
	public int getLen(Charset charset) {
		int len = 4+1+2;
		try{
			if(programmes!=null && programmes.length>0){
				for(ProgrammeDto pm : programmes){
					len += 1+4+1+4+2+1+4+1+pm.getLen(charset);
				}
			}
			if(channelName!=null && !"".equals(channelName)){
				len += channelName.getBytes(charset).length;
			}
		}catch(Exception e){
			logger.error("频道信息转换为字节码错误...", e);
		}
		return len;
	}

	@Override
	public int getDataOffset() {
		int len = 2+4+4+1+2;
		if(programmes!=null && programmes.length>0){
			len += programmes.length*(1+4+1+4+2+1+4+1);
		}
		return len;
	}

	public ProgrammeDto[] getProgrammes() {
		return programmes;
	}

	public void setProgrammes(ProgrammeDto[] programmes) {
		this.programmes = programmes;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
	

}

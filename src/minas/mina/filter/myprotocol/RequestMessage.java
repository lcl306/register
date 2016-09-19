package mina.filter.myprotocol;

import java.nio.charset.Charset;

import org.apache.log4j.Logger;

/**
 *            偏移地址(DataOffset)
 * ..................................................
 *  Tag    数据区length     基本数据区                                             真实数据区
 *  0x0001                ID(2B) channel_desc_len(B)
 * --2B---|----4B------|-----------------------------|-------------------------
 *                      .......................................................
 *                                    数据区len
 * */
public class RequestMessage extends AbsMessage {
	
	private Logger logger = Logger.getLogger(RequestMessage.class);
	
	private String channelDesc;
	
	private int channelId;

	@Override
	public short getTag() {
		return (short)0x0001;
	}

	@Override
	public int getLen(Charset charset) {
		int len = 2+1;
		try{
			if(channelDesc!=null && !"".equals(channelDesc)){
				len += channelDesc.getBytes(charset).length;
			}
		}catch(Exception e){
			logger.error("频道说明转换为字节码错误...", e);
		}
		return len;
	}

	@Override
	public int getDataOffset() {
		return 2+4+2+1;
	}

	public String getChannelDesc() {
		return channelDesc;
	}

	public void setChannelDesc(String channelDesc) {
		this.channelDesc = channelDesc;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

}

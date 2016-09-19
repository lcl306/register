package mina.filter.myprotocol;

import java.nio.charset.Charset;

/**
 *            偏移地址
 * .....................................
 *  Tag    数据区length     基本数据区          真实数据区
 * -------|------------|----------------|-------------------------
 *                      ..........................................
 *                                    数据区length
 * */
public abstract class AbsMessage {
	
	//协议编号
	public abstract short getTag();
	
	//数据区长度
	public abstract int getLen(Charset charset);
	
	//真实数据偏移地址
	public abstract int getDataOffset();

}

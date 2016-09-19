package minas.mina.filter.mytextline;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class MyTextLineCodecEncoder implements ProtocolEncoder {
	
	private Charset charset;
	
	private String delimiter;
	
	public MyTextLineCodecEncoder(Charset charset, String delimiter){
		this.charset = charset;
		this.delimiter = delimiter;
	}

	public void dispose(IoSession session) throws Exception {

	}

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out)throws Exception {
		delimiter = delimiter==null||"".equals(delimiter.trim())?"\r\n":delimiter;
		charset = charset==null?Charset.forName("utf-8"):charset;
		
		String value = message.toString();
		IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);
		
		buf.putString(message.toString(), charset.newEncoder());  //真实数据
		buf.putString(delimiter, charset.newEncoder());  //文本换行符
		buf.flip();
		out.write(buf);
	}

}

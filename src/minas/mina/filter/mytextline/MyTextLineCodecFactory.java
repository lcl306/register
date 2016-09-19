package minas.mina.filter.mytextline;

import java.nio.charset.Charset;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class MyTextLineCodecFactory implements ProtocolCodecFactory {

	private Charset charset;
	
	private String delimiter;
	
	public MyTextLineCodecFactory(Charset charset, String delimiter){
		this.charset = charset;
		this.delimiter = delimiter;
	}
	
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return new MyTextLineCodecDecoder(charset, delimiter);
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return new MyTextLineCodecEncoder(charset, delimiter);
	}

}

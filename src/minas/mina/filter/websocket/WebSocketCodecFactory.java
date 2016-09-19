/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mina.filter.websocket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Codec Factory used for creating websocket filter.
 * 
 * @author DHRUV CHOPRA
 */
public class WebSocketCodecFactory implements ProtocolCodecFactory{
    private ProtocolEncoder encoder;
    private ProtocolDecoder decoder;

    public WebSocketCodecFactory() {
            encoder = new WebSocketEncoder();
            decoder = new WebSocketDecoder();
    }
    
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
    	return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}
 
}

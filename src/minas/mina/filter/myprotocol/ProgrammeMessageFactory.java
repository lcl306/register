package minas.mina.filter.myprotocol;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class ProgrammeMessageFactory extends DemuxingProtocolCodecFactory {
	
	private ProgrammeMessageDecoder decoder;
	
	private ProgrammeMessageEncoder encoder;
	
	public ProgrammeMessageFactory(ProgrammeMessageDecoder decoder, ProgrammeMessageEncoder encoder){
		this.decoder = decoder;
		this.encoder = encoder;
		addMessageDecoder(this.decoder);
		addMessageEncoder(AbsMessage.class, this.encoder);
	}

}

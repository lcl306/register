package cindy.http.coder;

import java.nio.ByteBuffer;

import net.sf.cindy.Buffer;
import net.sf.cindy.Packet;
import net.sf.cindy.PacketEncoder;
import net.sf.cindy.Session;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.packet.DefaultPacket;
import net.sf.cindy.util.Charset;
import cindy.http.message.HttpResponse;

public class HttpResponseEncoder implements PacketEncoder {

	public Packet encode(Session session, Object obj) throws Exception {
		HttpResponse response = (HttpResponse)obj;
		
		ByteBuffer header = Charset.UTF8.encode(response.toString());
		Buffer buffer = null;
		int contentLen = response.getContent().length;
		if(contentLen>0){
			buffer = BufferFactory.allocate(header.remaining()+response.getContent().length);
			buffer.put(header).put(response.getContent()).flip();
		}else{
			buffer = BufferFactory.wrap(header);
		}
		return new DefaultPacket(buffer);
	}

}

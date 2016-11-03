package cindy.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

import net.sf.cindy.Buffer;
import net.sf.cindy.Packet;
import net.sf.cindy.PacketDecoder;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.util.ChannelUtils;
import net.sf.cindy.util.Charset;
import cindy.ServerInfo;

public class FileServerSessionHandler extends SessionHandlerAdapter {
	
	private FileChannel fc;
	private String fileName;
	
	public void objectReceived(Session session, Object obj)
		throws FileNotFoundException, IOException{
		Buffer buffer = (Buffer)obj;
		try{
			if(fc==null){
				fileName = buffer.getString(Charset.UTF8, buffer.remaining());
				System.out.println("Receiving "+fileName +" from " +session.getRemoteAddress());
				String realName = ServerInfo.SERVER_DIR +File.separator +fileName;
				fc = new RandomAccessFile(realName, "rw").getChannel();
			}else{
				while(buffer.hasRemaining())
					buffer.write(fc);
			}
		}finally{
			buffer.release();
		}
	}
	
	public void sessionStarted(Session session)throws Exception{
		System.out.println(session.getRemoteAddress() +" connected!");
	}
	
	public void sessionClosed(Session session)throws Exception{
		System.out.println("Received " +fileName);
		ChannelUtils.close(fc);
		fc = null;
	}
	
	public void exceptionCaught(Session session, Throwable cause){
		cause.printStackTrace();
	}
	
	static class FileTransferMessageDecoder implements PacketDecoder{
		public Object decode(Session session, Packet packet)throws Exception{
			// same as filter
			Buffer buffer = packet.getContent();
			if(buffer.remaining()>=2){
				int len = buffer.getUnsignedShort();
				if(buffer.remaining()>=len){
					Buffer content = BufferFactory.allocate(len);
					buffer.get(content);
					return content.flip();
				}
			}
			return null;
		}
	}

}

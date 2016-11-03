package cindy.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import net.sf.cindy.Buffer;
import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.util.ChannelUtils;
import net.sf.cindy.util.Charset;
import net.sf.cindy.util.ElapsedTime;

public class FileClientSessionHandler extends SessionHandlerAdapter {
	
	private final File file;
	private FileChannel fc;
	private ElapsedTime elapsedTime;
	
	// QUEUE_SIZE is count of buffer
	private static final int QUEUE_SIZE = 3;
    private static final int MESSAGE_SIZE = 65535;
	
	public FileClientSessionHandler(File file){
		this.file = file;
	}
	
	public void sessionStarted(Session session)throws Exception{
		elapsedTime = new ElapsedTime();
		if(fc == null)
			fc = new RandomAccessFile(file, "r").getChannel();
		sendName(session);
		sendContent(session);
	}
	
	private int sentCount;
	
	
	// when QUEUE_SIZE times, send content
	// obj is cindy's buffer, buffer would read ByteChannel constantly until readCount = -1
	public void objectSent(Session session, Object obj)throws Exception{
//		System.out.println("send file " +sentCount);
		if(++sentCount % QUEUE_SIZE == 1){
			sendContent(session);
		}		
	}
	
	public void sessionClosed(Session session)throws Exception{
		if(elapsedTime != null)
			System.out.println("elapsed time: " +elapsedTime.getElapsedTime() +" ms");
		elapsedTime = null;
		ChannelUtils.close(fc);
		fc = null;
	}
	
	public void exceptionCaught(Session session, Throwable cause){
		cause.printStackTrace();
	}
	
	private void sendName(Session session){
		ByteBuffer name = Charset.UTF8.encode(file.getName());
		// 2 is the size of file
		Buffer content = BufferFactory.allocate(name.remaining()+2)
			.putUnsignedShort(name.remaining()).put(name).flip();
		session.send(content);
	}
	
	
	// send QUEUE_SIZE's buffer one time
	private void sendContent(Session session)throws IOException{
		for(int i=0; i<QUEUE_SIZE; i++){
			Buffer buffer = BufferFactory.allocate(MESSAGE_SIZE);
			buffer.position(2);
			int readCount = buffer.read(fc);
			if(readCount == -1){
				buffer.release();
				// clear BufferFactory
				session.send(BufferFactory.allocate(0)).addListener(new FutureListener(){
					public void futureCompleted(Future future) throws Exception {
						future.getSession().close();
					}
				});
				break;
			}else{
				// position 0 and 1 is file size
				buffer.putUnsignedShort(0, readCount).flip();
				session.send(buffer);
			}
		}
	}

}

package cindy.chat;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import net.sf.cindy.Buffer;
import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Packet;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.buffer.BufferFactory;
import net.sf.cindy.util.ChannelUtils;
import net.sf.cindy.util.ElapsedTime;

public class ChatClientHandler extends SessionHandlerAdapter {
	
	private ElapsedTime elapsedTime;
	
	private ReadableByteChannel rbc;
	
	private static final int MAX_SIZE = 1024;
	
	public void sessionStarted(Session session)throws Exception{
		elapsedTime = new ElapsedTime();
		rbc = Channels.newChannel(System.in);
		// the first send
		sendMessage(session);
	}
	
	public void objectSent(Session session, Object obj)throws Exception{
		sendMessage(session);
	}
	
	public void objectReceived(Session session, Object obj)throws Exception{
		Packet packet = (Packet)obj;
		receiveMessage(packet);
	}
	
	public void sessionClosed(Session session)throws Exception{
		if(elapsedTime != null)
			System.out.println("elapsed time: " +elapsedTime.getElapsedTime() +" ms");
		elapsedTime = null;
		ChannelUtils.close(rbc);
		rbc = null;
	}
	
	public void exceptionCaught(Session session, Throwable cause){
		cause.printStackTrace();
	}
	
	private void receiveMessage(Packet packet){
		Buffer buffer = packet.getContent();
		byte[] temp = new byte[buffer.limit()];
		for(int i=0; i<temp.length; i++){
			temp[i] = buffer.get(i);
		}
		System.out.println(new String(temp));
	}
	
	private void sendMessage(Session session)throws IOException{
		Buffer buffer = BufferFactory.allocate(MAX_SIZE);
		int pos = buffer.read(rbc);
		// the two end char is /r and /n
		pos = pos -2;
		BufferFactory.allocate(pos);
		byte[] temp = new byte[pos];
		for(int i=0; i<temp.length; i++){
			temp[i] = buffer.get(i);
		}
		String line = new String(temp);
		//System.out.println("real send: " +line);
		if(line.trim().equalsIgnoreCase("bye")){
			buffer.release();
			session.send(BufferFactory.allocate(0)).addListener(new FutureListener(){
				public void futureCompleted(Future future) throws Exception {
					future.getSession().close();
				}
			});
		}else{
			session.send(buffer.flip());
		}
	}

}

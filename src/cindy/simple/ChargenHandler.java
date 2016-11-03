package cindy.simple;

import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.packet.DefaultPacket;

public class ChargenHandler extends SessionHandlerAdapter {
	
	//ASCII printing characters
	private static byte[] DATA = new byte[94];
	
	static{
		for(int i=0; i<DATA.length; i++){
			DATA[i] = (byte)(33+i);
		}
	}
	
	public void sessionStarted(Session session)throws Exception{
		send(session);
	}
	
	private void send(Session session){
//		session.flush(new DefaultPacket(DATA)).addListener(new FutureListener(){
//			public void futureCompleted(Future future)throws Exception{
//				if(future.isSucceeded()) send(future.getSession());
//			}
//		});
		session.flush(new DefaultPacket(DATA));
	}

}

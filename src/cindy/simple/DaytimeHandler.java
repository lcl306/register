package cindy.simple;

import java.nio.ByteBuffer;
import java.util.Date;

import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.packet.DefaultPacket;
import net.sf.cindy.util.Charset;

public class DaytimeHandler extends SessionHandlerAdapter {
	
	public void sessionStarted(Session session)throws Exception{
		ByteBuffer buffer = Charset.UTF8.encode(new Date().toString());
		Future future = session.flush(new DefaultPacket(buffer));
		future.addListener(new FutureListener(){
			public void futureCompleted(Future future)throws Exception{
				future.getSession().close();
			}
		});
	}

}

package cindy.simple;

import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;

public class EchoHandler extends SessionHandlerAdapter {
	
	public void objectReceived(Session session, Object obj)throws Exception{
		session.send(obj);
	}

}

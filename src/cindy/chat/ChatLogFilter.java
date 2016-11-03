package cindy.chat;

import java.net.SocketAddress;

import net.sf.cindy.Session;
import net.sf.cindy.SessionFilterAdapter;
import net.sf.cindy.SessionFilterChain;

public class ChatLogFilter extends SessionFilterAdapter {

	private static final Object ADDR_ATTR = "remoteAddr";
	
	public void sessionStarted(SessionFilterChain filterChain)throws Exception{
		Session session = filterChain.getSession();
		SocketAddress address = session.getRemoteAddress();
		session.setAttribute(ADDR_ATTR, address);
		System.out.println("log: " +address +" log in");
		super.sessionStarted(filterChain);
	}
	
	public void sessionClosed(SessionFilterChain filterChain)throws Exception{
		System.out.println(filterChain.getSession().getAttribute(ADDR_ATTR) +" log out");
		super.sessionClosed(filterChain);
	}
	
	public void sessionTimeout(SessionFilterChain filterChain)throws Exception{
		System.out.println(filterChain.getSession().getAttribute(ADDR_ATTR) +" timeout");
		super.sessionTimeout(filterChain);
	}
	
	public void exceptionCaught(SessionFilterChain filterChain, Throwable cause){
		System.err.println(filterChain.getSession().getAttribute(ADDR_ATTR) +" " +cause);
		super.exceptionCaught(filterChain, cause);
	}

}

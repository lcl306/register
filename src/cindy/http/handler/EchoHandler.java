package cindy.http.handler;

import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.util.Charset;
import cindy.http.message.HttpRequest;
import cindy.http.message.HttpResponse;

public class EchoHandler extends SessionHandlerAdapter {
	
	public void objectReceived(Session session, Object obj)throws Exception{
		HttpRequest request = (HttpRequest)obj;
		byte[] header = Charset.UTF8.encodeToArray(request.toString());
		boolean keepAlive = "keep-alive".equalsIgnoreCase(request.getParam("Connection"));
		
		HttpResponse response = new HttpResponse();
		response.setStatusCode(200);
		response.setVersion(request.getVersion());
		response.setReasonPhrase("OK");
		response.setParam("Server", "Cindy Http Server");
		response.setParam("Content-Type", "text/plain");
		response.setParam("Content-Length", String.valueOf(header.length));
		response.setParam("Connection", keepAlive?"keep-alive":"close");
		response.setContent(header);
		
		Future future = session.send(response);
		if(!keepAlive){
			future.addListener(new FutureListener(){
				public void futureCompleted(Future future)throws Exception{
					future.getSession().close();
				}
			});
		}
	}
	
	public void exceptionCaught(Session session, Throwable cause){
		session.close();
		System.err.println(cause);
	}

}

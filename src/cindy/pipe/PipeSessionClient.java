package cindy.pipe;

import java.io.Serializable;

import net.sf.cindy.Future;
import net.sf.cindy.FutureListener;
import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.decoder.SerialDecoder;
import net.sf.cindy.encoder.SerialEncoder;
import net.sf.cindy.session.nio.PipeSession;

public class PipeSessionClient {
	
	public static void send(){
		Session session = new PipeSession();
		session.setPacketDecoder(new SerialDecoder());
		session.setPacketEncoder(new SerialEncoder());
		
		session.setSessionHandler(new SessionHandlerAdapter(){
			public void sessionStarted(Session session)throws Exception{
				System.out.println("session started!");
			}
			public void sessionClosed(Session session)throws Exception{
				System.out.println("session closed!");
			}
			public void objectReceived(Session session, Object obj)throws Exception{
				System.out.println("received " +obj.getClass().getName()+": " +obj);
			}
		});
		session.start().complete();
		
		session.send("hello world");
		session.send(new Integer(Integer.MAX_VALUE));
		session.send(new Double(Math.random()));
		session.send(new User("User 1", 20));
		session.send("bye").addListener(new FutureListener(){
			public void futureCompleted(Future future) throws Exception {
				future.getSession().close();
			}
		});
	}
	
	public static void main(String[] args){
		send();
	}
	
	private static class User implements Serializable{
		private String name;
		private int age;
		
		public User(String name, int age){
			this.age = age;
			this.name = name;
		}
		public String toString(){
			return "name=" +name +" age=" +age;
		}
	}

}

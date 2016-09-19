package minas.mina;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import minas.mina.filter.myprotocol.ProgrammeMessageDecoder;
import minas.mina.filter.myprotocol.ProgrammeMessageEncoder;
import minas.mina.filter.myprotocol.ProgrammeMessageFactory;
import minas.mina.filter.websocket.WebSocketCodecFactory;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class ClientUtil {
	
	private static Logger logger = Logger.getLogger(ClientUtil.class);
	
	public static void sendWebsocket(String ip, int port, IoHandlerAdapter handler, Object message){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("WSCodec", new ProtocolCodecFilter(new WebSocketCodecFactory()));
		filters.put("TextLineCodec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		filters.put("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		send(ip, port, filters, handler, new MessageSendBack(message));
	}
	
	public static void sendProgramme(String ip, int port, IoHandlerAdapter handler, Object message){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("codec", new ProtocolCodecFilter(new ProgrammeMessageFactory(new ProgrammeMessageDecoder(Charset.forName("utf-8")), new ProgrammeMessageEncoder(Charset.forName("utf-8")))));
		send(ip, port, filters, handler, new MessageSendBack(message));
	}
	
	public static void sendLine(String ip, int port, IoHandlerAdapter handler, String message){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
		//filters.put("codec", new ProtocolCodecFilter(new MyTextLineCodecFactory(Charset.forName("utf-8"), "\r\n")));
		send(ip, port, filters, handler, new MessageSendBack(message));
	}
	
	public static void sendObject(String ip, int port, IoHandlerAdapter handler, Object message){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		send(ip, port, filters, handler, new MessageSendBack(message));
	}
	
	static class MessageSendBack implements SendBack{
		
		private Object message;
		
		public MessageSendBack(Object message){
			this.message = message;
		}
		
		public void exec(IoSession session) {
			session.write(message);
		}
	} 
	
	public static void send(String ip, int port, Map<String, IoFilter> filters, IoHandlerAdapter handler, SendBack sendBack){
		IoConnector connector = new NioSocketConnector(); //非阻塞客户端
		connector.setConnectTimeoutMillis(30000);  //连接超时时间
		for(Entry<String, IoFilter> entry : filters.entrySet()){
			connector.getFilterChain().addLast(entry.getKey(), entry.getValue());
		}
		connector.setHandler(handler);
		IoSession session = null;
		try{
			ConnectFuture future = connector.connect(new InetSocketAddress(ip, port));
			future.awaitUninterruptibly();  //等待连接创建完成，该操作是一个阻塞操作
			session = future.getSession();
			sendBack.exec(session);
		}catch(Exception e){
			logger.error("client error: ", e);
		}
		session.getCloseFuture().awaitUninterruptibly(); //必须等待服务端连接断开
		connector.dispose();
	}
	
	static interface SendBack{
		public void exec(IoSession session);
	}

}

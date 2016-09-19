package mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import mina.filter.myprotocol.ProgrammeMessageDecoder;
import mina.filter.myprotocol.ProgrammeMessageEncoder;
import mina.filter.myprotocol.ProgrammeMessageFactory;
import mina.filter.websocket.WebSocketCodecFactory;

import org.apache.log4j.Logger;
import org.apache.mina.core.filterchain.IoFilter;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class ServerUtil {
	
	private static Logger logger = Logger.getLogger(ServerUtil.class);
	
	public static void startWebSocketExer(int port, IoHandlerAdapter handler){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("WSCodec", new ProtocolCodecFilter(new WebSocketCodecFactory()));
		filters.put("TextLineCodec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		filters.put("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));
		start(port, filters, handler);
	}
	
	public static void startProgammeExer(int port, IoHandlerAdapter handler){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("codec", new ProtocolCodecFilter(new ProgrammeMessageFactory(new ProgrammeMessageDecoder(Charset.forName("utf-8")), new ProgrammeMessageEncoder(Charset.forName("utf-8")))));
		/*LoggingFilter lf = new LoggingFilter();
		lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
		filters.put("logger", lf);*/
		start(port, filters, handler);
	}
	
	public static void startLineExer(int port, IoHandlerAdapter handler){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),LineDelimiter.WINDOWS.getValue(), LineDelimiter.WINDOWS.getValue())));
		//filters.put("codec", new ProtocolCodecFilter(new MyTextLineCodecFactory(Charset.forName("utf-8"), "\r\n")));
		LoggingFilter lf = new LoggingFilter();
		lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
		filters.put("logger", lf);
		start(port, filters, handler);
	}
	
	public static void startObjectExer(int port, IoHandlerAdapter handler){
		Map<String, IoFilter> filters = new LinkedHashMap<String, IoFilter>();
		filters.put("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		start(port, filters, handler);
	}
	
	public static void start(int port, Map<String, IoFilter> filters, IoHandlerAdapter handler){
		IoAcceptor acceptor = null;
		try{
			acceptor = new NioSocketAcceptor(); //非阻塞
			// addLast是将Filter放在FilterChain的最后（addLast可以顺序执行加入的Filter），addFirst是放置最前，addBefore是放在前面
			for(Entry<String, IoFilter> entry : filters.entrySet()){
				acceptor.getFilterChain().addLast(entry.getKey(), entry.getValue());
			}
			acceptor.getSessionConfig().setReadBufferSize(2048); //读写缓冲区大小，不设，mina会自动调节，setMinReadBufferSize()、setMaxReadBufferSize()是区间
			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10); //通道10秒无操作，进入空闲状态，且每隔10秒调用IOHandler的sessionIdle
			acceptor.getSessionConfig().setWriteTimeout(60);     //写超时1分钟
			acceptor.setHandler(handler);
			acceptor.bind(new InetSocketAddress(port));
			logger.info("server started, port: " +port);
		}catch(IOException e){
			logger.error("server stated error", e);
			e.printStackTrace();
		}
	}
	
}

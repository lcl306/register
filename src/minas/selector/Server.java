package minas.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class Server {
	
	public static void start(int port){
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(port));
			ssc.configureBlocking(false);
			Selector selector = Selector.open();
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			Tcpnio.doSeletorServer(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

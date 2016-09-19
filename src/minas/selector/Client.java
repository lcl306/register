package minas.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class Client {
	
	public static void send(String hostname, int port){
		try {
			SocketChannel channel = SocketChannel.open();
			channel.configureBlocking(false);	//非阻塞
			channel.connect(new InetSocketAddress(hostname, port)); // InetAddress配置ip，InetSocketAddress配置ip+port(0-65535)
			Selector selector = Selector.open();
			channel.register(selector, SelectionKey.OP_CONNECT);   //channel注册selector
			Tcpnio.doSeletor(selector);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

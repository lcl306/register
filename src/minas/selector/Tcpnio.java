package minas.selector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Tcpnio {
	
	public static long timeout = 60000;
	
	public static final int BUFFER_SIZE = 1024;
	
	/**
	 * selector模式，用于存在大量连接，且连接不频繁的情况下使用
	 * */
	static void doSeletor(Selector selector) throws IOException{
		int nKeys = timeout==0l?selector.select():selector.select(timeout);  // 有timeout是阻塞至timeout，无timeout是一致阻塞至IO事件发生
		if(nKeys>0){	// nKeys > 0, 说明有IO事件发生
			Set<SelectionKey> keys = selector.selectedKeys();
			// key是selector的状态
			for(SelectionKey key : keys){	
				// 用于client，完成了握手连接，将selector监听的channel设置为OP_READ
				if(key.isConnectable()){
					SocketChannel sc = (SocketChannel) key.channel();
					sc.configureBlocking(false);
					sc.register(selector, SelectionKey.OP_READ);  //不注册OP_WRITE，channel未满，一直可写
					sc.finishConnect();  //完成连接建立
				}
				// 有流可读取，将channel中的数据写入buffer
				else if(key.isReadable()){
					ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
					SocketChannel sc = (SocketChannel) key.channel();
					int readBytes = 0;
					int ret = 0;
					try {
						while ((ret = sc.read(buffer)) > 0) { // 将channel中的流数据读入buffer中，buffer满 ==0 阻塞, ==-1流结尾
							readBytes += ret;
						}
					} finally {
						buffer.flip();
						System.out.println(readBytes);
						buffer.clear();
					}
				}
				// 有写入流，将buffer数据写入channel
				else if(key.isWritable()){
					key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE)); // 取消OP_WRITE的注册事件
					SocketChannel sc = (SocketChannel)key.channel();
					String sendString="你好,服务器. ";
					System.out.println(sendString);
					ByteBuffer buffer=ByteBuffer.wrap(sendString.getBytes("UTF-16"));
					int writtenSize = sc.write(buffer);  // 写入channel已满，writtenSize=0，阻塞
					// 如果还有未写完的数据，重新注册写事件
					if(writtenSize==0){
						key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
					}
				}
			}
			selector.selectedKeys().clear();
		}
	}
	
	
	static void doSeletorServer(Selector selector) throws IOException{
		int nKeys = timeout==0l?selector.select():selector.select(timeout);  // 有timeout是阻塞至timeout，无timeout是一致阻塞至IO事件发生
		if(nKeys>0){	// nKeys > 0, 说明有IO事件发生
			Set<SelectionKey> keys = selector.selectedKeys();
			// key是selector的状态
			for(SelectionKey key : keys){
				// 用于server，表明可接受，将selector的监听的channel设置为OP_READ
				if(key.isAcceptable()){
					ServerSocketChannel server = (ServerSocketChannel)key.channel();
					SocketChannel sc = server.accept();
					if(sc==null) continue;
					sc.configureBlocking(false);
					Selector sel = Selector.open();
					sc.register(sel, SelectionKey.OP_READ);
					doSeletor(sel);
				}
			}
		}
	}

}

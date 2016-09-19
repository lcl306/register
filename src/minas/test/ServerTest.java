package minas.test;

import minas.mina.ServerUtil;


public class ServerTest {

	private static int PORT = 1028;
	
	public static void main(String[] args) throws Exception{
		//Server.start(15000);
		//ServerUtil.startLineExer(PORT, new StringHandler("server"));
		//ServerUtil.startObjectExer(PORT, new ObjectHandler("server"));
		//ServerUtil.startProgammeExer(PORT, new ProgrammeServerHandler());
		ServerUtil.startWebSocketExer(PORT, new WebsocketServerHanlder());
	}

}

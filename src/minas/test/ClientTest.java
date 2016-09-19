package test;

import mina.ClientUtil;
import mina.filter.myprotocol.RequestMessage;

public class ClientTest {
	
	private static String HOST = "127.0.0.1";
	
	private static int PORT = 1028;
	
/*	private static String HOST = "123.157.249.202";
	
	private static int PORT = 20004;*/
	
	public static void main(String[] args) throws Exception{
		//Client.send("127.0.0.1", 15000);
		//ClientUtil.sendLine(HOST, PORT, new StringHandler("client"), "你好mina");
		//testObject();
		//testProgramme();
		//ClientUtil.sendWebsocket("123.157.249.202", 20006, new StringHandler("client"), "nodeData");
		ClientUtil.sendWebsocket(HOST, PORT, new WebsocketClientHanlder(), "nodeData");
	}
	
	public static void testObject(){
		PhoneDto dto = new PhoneDto();
		dto.setSendCode("123456789");
		dto.setReceiveCode("987654321");
		dto.setMessage("短消息");
		ClientUtil.sendObject(HOST, PORT, new ObjectHandler("client"), dto);
	}
	
	public static void testProgramme(){
		RequestMessage req = new RequestMessage();
		req.setChannelId(123456);
		req.setChannelDesc("mina programme");
		ClientUtil.sendProgramme(HOST, PORT, new ProgrammeClientHandler(), req);
	}

}

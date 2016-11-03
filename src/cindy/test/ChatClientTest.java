package cindy.test;

import cindy.ServerInfo;
import cindy.chat.ChatClient;

public class ChatClientTest{

	public static void main(String[] args) {
		//chatSend();
		//echoSend();
		//charSend();
		daytimeSend();
	}
	
	public static void chatSend(){
		ChatClient.send("localhost", ServerInfo.CHAT_SERVER_PORT);
	}
	
	public static void echoSend(){
		ChatClient.send("localhost", ServerInfo.ECHO_PORT);
	}
	
	public static void charSend(){
		ChatClient.send("localhost", ServerInfo.CHARGEN_PORT);
	}
	
	public static void daytimeSend(){
		ChatClient.send("localhost", ServerInfo.DAYTIME_PORT);
	}

}

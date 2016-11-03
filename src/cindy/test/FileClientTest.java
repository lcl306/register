package cindy.test;

import cindy.file.FileClient;

public class FileClientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String filename = "H:/game/sgqyz3.rar";
		String ip = "localhost";
		
		FileClient.send(filename, ip);

	}

}

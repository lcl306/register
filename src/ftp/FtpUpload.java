package ftp;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

import net.sf.jftp.net.BasicConnection;
import net.sf.jftp.net.ConnectionHandler;
import net.sf.jftp.net.ConnectionListener;
import net.sf.jftp.net.FtpConnection;
import net.sf.jftp.system.logging.Log;

import org.apache.commons.lang.StringUtils;

public class FtpUpload implements ConnectionListener {

	private boolean isThere = false;

	private ConnectionHandler handler = new ConnectionHandler();

	private String host;

	private int port = 21;

	private String user;

	private String passwd;

	public FtpUpload(String host, String user, String passwd) {
		this.host = host;
		this.user = user;
		this.passwd = passwd;
	}

	public FtpUpload(String host, int port, String user, String passwd) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.passwd = passwd;
	}
	
	
	 private static void setSocksProxyOptions(String proxy, String port)
	    {
	        if(proxy.equals("") || port.equals(""))
	        {
	            return;
	        }

	        java.util.Properties sysprops = System.getProperties();

	        // Remove previous values
	        sysprops.remove("socksProxyHost");
	        sysprops.remove("socksProxyPort");

	        // Set your values
	        sysprops.put("socksProxyHost", proxy);
	        sysprops.put("socksProxyPort", port);

	        Log.out("socks proxy: " + sysprops.get("socksProxyHost") + ":" +
	                sysprops.get("socksProxyPort"));
	    }
	

	public int upload(String dir, String file) {
		
//		setSocksProxyOptions(Settings.getSocksProxyHost(),
//				Settings.getSocksProxyPort());
		
		
		FtpConnection con = new FtpConnection(host, port, "/");

		con.addConnectionListener(this);

		con.setConnectionHandler(handler);

		con.login(user, passwd);

		while (!isThere) {
			try {
				Thread.sleep(10);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		//make dirs   
		String path = "";
		String[] paths = StringUtils.split(dir, "/");
		for (int i = 0; i < paths.length; i++) {
			path += "/" + paths[i];
			if (!con.chdir(path)) {
				con.mkdir(path);
			}
		}
		
		Vector files = con.currentFiles;
		for(Iterator it = files.iterator(); it.hasNext();){
			File f = (File)it.next();
			System.out.println("=================================" +f.getName());
		}
		
		con.chdir(dir);
		files = con.currentFiles;
		for(Iterator it = files.iterator(); it.hasNext();){
			File f = (File)it.next();
			System.out.println("=================================" +f.getName());
		}
		
		boolean rtn = con.mkdir("test3");
		if(rtn) System.out.println("=======================================> make dir sucess");
		return con.upload(file);
		//return con.upload(file);
	}

	public static void main(String argv[]) {
//		if (argv.length == 3) {
//			FtpUpload f = new FtpUpload(argv[0], argv[2], argv[1]);
//		} else {
			FtpUpload g = new FtpUpload("192.168.1.105", "sunrex", "rexass");
			g.upload("/test", "C:/TDdownload/000006_20080520_SHIHARAIANNAI.pdf");
		//}
	}

	public void updateRemoteDirectory(BasicConnection con) {
		System.out.println("new path is: " + con.getPWD());
	}

	public void connectionInitialized(BasicConnection con) {
		isThere = true;
	}

	public void updateProgress(String file, String type, long bytes) {
	}

	public void connectionFailed(BasicConnection con, String why) {
		System.out.println("connection failed!");
	}

	public void actionFinished(BasicConnection con) {
	}
}   


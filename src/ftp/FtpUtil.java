package ftp;

import net.sf.jftp.net.BasicConnection;
import net.sf.jftp.net.ConnectionHandler;
import net.sf.jftp.net.ConnectionListener;
import net.sf.jftp.net.FtpConnection;

import org.apache.commons.lang.StringUtils;

public class FtpUtil implements ConnectionListener{
	
	private boolean isThere = false;
	
	private ConnectionHandler handler = new ConnectionHandler();
	
	private String host;
	
	private int port;
	
	private String user;
	
	private String passwd;
	
	public FtpUtil(String host, int port, String user, String passwd){
		this.host = host;
		this.port = port;
		this.user = user;
		this.passwd = passwd;
	}
	
	public FtpUtil(String host, String user, String passwd){
		this(host, 21, user, passwd);
	}
	
	protected FtpConnection getConnection(boolean isUnix){
		String baseDir = isUnix?"/":"";
		FtpConnection con = new FtpConnection(host, port, baseDir);
		con.addConnectionListener(this);
		con.setConnectionHandler(handler);
		con.login(user, passwd);
		while(!isThere){
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return con;
	}
	
	protected String gotoPath(FtpConnection con, String remoteDir){
		String path = con.getPWD();
		String[] dirParts = StringUtils.split(remoteDir, "/");
		for(int i=0; i<dirParts.length; i++){
			path = path + "/" +dirParts[i];
			if(!con.chdir(path)){
				con.mkdir(path);
			}
		}
		con.chdir(path);
		return path;
	}
	
	public int upload(String remoteDir, String localFile){
		FtpConnection con = this.getConnection(false);
		this.gotoPath(con, remoteDir);
		return con.upload(localFile);
	}
	
	private String[] getDirAndFile(String fileName){
		String[] rtn = new String[2];
		if(fileName!=null && fileName.lastIndexOf("/")!=-1){
			rtn[0] = fileName.substring(0, fileName.lastIndexOf("/"));
			if(!fileName.endsWith("/"))
				rtn[1] = fileName.substring(fileName.lastIndexOf("/")+1);
			else
				rtn[1] = "";
			return rtn;
		}else{
			return new String[]{"", fileName}; 
		}
	}
	
	public int download(String remoteFile, String localName){
		FtpConnection con = this.getConnection(false);
		String[] dirAndFile = getDirAndFile(localName);
		con.setLocalPath(dirAndFile[0]);
		String fileName = dirAndFile[1];
		this.gotoPath(con, getDirAndFile(remoteFile)[0]);
		return con.download(remoteFile, fileName);
	}
	
	public int delFileOrDir(String remoteFile){
		FtpConnection con = this.getConnection(false);
		String[] dirAndFile = getDirAndFile(remoteFile);
		this.gotoPath(con, dirAndFile[0]);
		return con.removeFileOrDir(dirAndFile[1]);
	}

	public void updateRemoteDirectory(BasicConnection con) {
		//System.out.println("new path is: " +con.getPWD());
	}

	public void updateProgress(String arg0, String arg1, long arg2) {
	}

	public void connectionInitialized(BasicConnection arg0) {
		isThere = true;
	}

	public void connectionFailed(BasicConnection arg0, String arg1) {
		System.out.println("connection failed!");
	}

	public void actionFinished(BasicConnection arg0) {
	}
	
	

}

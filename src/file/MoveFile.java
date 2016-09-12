package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MoveFile {

	static String[] excludeString = {"Failed password for ", "fatal: Read from socket failed", "more authentication failure", "PAM service(sshd) ignoring max retries", 
			"Invalid user ", "input_userauth_request: invalid user", "pam_unix(sshd:auth): check pass; user unknown", "pam_unix(sshd:auth): authentication failure; logname=", 
			"pam_succeed_if(sshd:auth): error retrieving information about user", "Failed password for invalid user ", "Did not receive identification string from", 
			"Received disconnect from", "Connection closed by", "Bad protocol version identification",
			"pam_unix(vsftpd:auth): check pass; user unknown", "pam_unix(vsftpd:auth): authentication failure", "pam_succeed_if(vsftpd:auth): error retrieving information about user"};
	
	
	static String[] mysqlExcludString = {"STATEMENT", "INSERT", "DELETE", "UPDATE", "insert", "delete", "update"};
	
	public static void copyTo(String inName, String outName, String[] excludeString) throws Exception{
		FileInputStream in = new FileInputStream(inName);
		InputStreamReader r = new InputStreamReader(in);
		BufferedReader b = new BufferedReader(r);
		FileOutputStream out = new FileOutputStream(outName);
		OutputStreamWriter w = new OutputStreamWriter(out);
		BufferedWriter br = new BufferedWriter(w);
		String line="";
		boolean match=false;
		while((line=b.readLine())!=null){
			for(String e : excludeString){
				if(line.indexOf(e)>0){
					match=true;
				}
			}
			if(!match && line.trim().length()>6 && !line.startsWith(" ") && !line.startsWith("\t")){
				br.write(line+"\r\n");
			}
			match=false;
		}
		br.close();
		w.close();
		out.close();
		b.close();
		r.close();
		in.close();
	}
	
	public static void main(String[] args) throws Exception{
		//copyTo("D:\\secure", "D:\\secure-1", excludeString);
		copyTo("D:\\WIN-8CKI7NEC7NU.err", "D:\\WIN-8CKI7NEC7NU.err-1", mysqlExcludString);
	}
	
}

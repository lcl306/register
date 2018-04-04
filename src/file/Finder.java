package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Finder {
	
	private static final String outputFile = "C:/Users/liuch/Desktop/logs/out.txt";
	
	public static void main(String[] args)throws Exception{
		//find(new File("C:/文档/大福/_说明文档/WMS3.5/daifuku/wms/src/sqlscript"), "utf-8", "audit_trail", null);
		find(new File("C:/Users/liuch/Desktop/logs"), "utf-8", "Agc	[0-9]{4}1200", null);
	}
	
	public static final String[] EXCLUDE_FILES = {".svn"};
	
	public static final int ENTER_LEN = 2;
	
	public static void find(File dir, String srcCode, String targetStr, String replaceStr)throws Exception{
		OutputStream out = new FileOutputStream(outputFile);
		OutputStreamWriter or = new OutputStreamWriter(out, srcCode);
		BufferedWriter bw = new BufferedWriter(or);
		if(dir.isDirectory() && dir.exists()){
			File[] files = dir.listFiles();
			for(File f : files){
				if(f.exists() && !isExcludeFile(f) && !f.getPath().equalsIgnoreCase(new File(outputFile).getPath())){
					if(f.isDirectory()){
						find(f, srcCode, targetStr, replaceStr);
					}else{
						doFind(f, srcCode, targetStr, replaceStr, bw);
					}	
				}
			}
		}else if(dir.exists() && dir.isFile()){
			doFind(dir, srcCode, targetStr, replaceStr, bw);
		}
		bw.close();
		or.close();
		out.close();
	}
	
	private static void doFind(File f, String srcCode, String targetStr, String replaceStr, BufferedWriter bw)throws Exception{
		InputStream in = new FileInputStream(f);
		InputStreamReader ir = new InputStreamReader(in, srcCode);
		BufferedReader br = new BufferedReader(ir);
		RandomAccessFile raf = new RandomAccessFile(f,"rw");
		String line = "";
		int count = 0;
		long pointer = 0;
		while((line=br.readLine())!=null){
			if(findStr(line, targetStr)){
				count++;
				if(replaceStr!=null){
					line = line.replaceAll(targetStr, replaceStr);
	                raf.seek(pointer);
	                raf.writeBytes(line);
				}
				bw.write(line+"\r\n");
			}
			pointer+=(line.length()+ENTER_LEN); //pointer总是行首，加2表示上一行的回车和换行符
            raf.seek(pointer);//定位位置
		}
		if(count>0){
			System.out.println(f.getPath()+": Number of "+targetStr+"="+count);
		}
		br.close();
		ir.close();
		in.close();
		if(raf!=null) raf.close();
	}
	
	private static boolean findStr(String line, String targetStr){
		Pattern pt=Pattern.compile(targetStr); 
		Matcher mt=pt.matcher(line);
		return mt.find();
	}
	
	private static boolean isExcludeFile(File file){
		for(String excludeF : EXCLUDE_FILES){
			if(file!=null && file.getName().equals(excludeF)) return true;
		}
		return false;
	}

}

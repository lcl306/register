package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class Finder {
	
	
	public static void main(String[] args)throws Exception{
		find(new File("C:/daifuku/wms/sqlscript/oracle/5.backup"), "utf-8", "D:", null);
		//find(new File("C:/daifuku"), "utf-8", "11.2.0", null);
	}
	
	public static final String[] EXCLUDE_FILES = {".svn"};
	
	public static final int ENTER_LEN = 2;
	
	public static void find(File dir, String srcCode, String targetStr, String replaceStr)throws Exception{
		if(dir.isDirectory() && dir.exists()){
			File[] files = dir.listFiles();
			for(File f : files){
				if(f.exists() && !isExcludeFile(f)){
					if(f.isDirectory()){
						find(f, srcCode, targetStr, replaceStr);
					}else{
						InputStream in = new FileInputStream(f);
						InputStreamReader ir = new InputStreamReader(in, srcCode);
						BufferedReader br = new BufferedReader(ir);
						RandomAccessFile raf = new RandomAccessFile(f,"rw");
						String line = "";
						int count = 0;
						long pointer = 0;
						while((line=br.readLine())!=null){
							int pos = line.indexOf(targetStr);
							if(pos!=-1){
								count++;
								if(replaceStr!=null){
									line = line.replaceAll(targetStr, replaceStr);
					                raf.seek(pointer);
					                raf.writeBytes(line);
								}
							}
							pointer+=(line.length()+ENTER_LEN); //pointer总是行首，加2表示上一行的回车和换行符
	                        raf.seek(pointer);//定位位置
						}
						if(count>0){
							System.out.println(f.getPath()+File.separator+f.getName()+": Number of "+targetStr+"="+count);
						}
						br.close();
						ir.close();
						in.close();
						if(raf!=null) raf.close();
					}	
				}
			}
		}
	}
	
	private static boolean isExcludeFile(File file){
		for(String excludeF : EXCLUDE_FILES){
			if(file!=null && file.getName().equals(excludeF)) return true;
		}
		return false;
	}

}

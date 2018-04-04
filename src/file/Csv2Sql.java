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

import org.apache.commons.lang.StringUtils;

public class Csv2Sql {
	
	public static String STD_CODE = "Shift_JIS";
	
	public static String TAGET_FILE_NAME = "asrs_conf.sql";
	
	public static void main(String[] args)throws Exception{
		doDir("C:/文档/123456");
	}
	
	public static void doDir(String dir)throws Exception{
		File[] files = new File(dir).listFiles();
		File tFile = new File(dir+File.separator+TAGET_FILE_NAME);
		if(tFile.exists()){
			tFile.delete();
		}
		tFile.createNewFile();
		for(File file : files){
			doFile(file, tFile);
		}
	}
	
	public static void doFile(File sfile, File tFile)throws Exception{
		InputStream in = new FileInputStream(sfile);
		InputStreamReader ir = new InputStreamReader(in, STD_CODE);
		BufferedReader br = new BufferedReader(ir);
		OutputStream out = new FileOutputStream(tFile, true);
		OutputStreamWriter iw = new OutputStreamWriter(out, STD_CODE); 
		BufferedWriter bw = new BufferedWriter(iw);
		String line = "";
		int i=0;
		String title = "";
		String[] titleParts = null;
		while((line=br.readLine())!=null){
			i++;
			if(i==2){
				title = line;
				titleParts = title.split(",");
			}else if(i>2){
				String[] parts = line.split(",");
				String tLine = "";
				int j=0;
				for(String part : parts){
					if(titleParts[j].indexOf("_DATE")!=-1){
						if(part.trim().length()>0) part = "sysdate";
						else part = "null";
					}else if(!isNumber(part)){ 
						part = "'" +part +"'";
					}
					tLine += (part+",");
					j++;
				}
				if(tLine.endsWith(",")) tLine = tLine.substring(0, tLine.length()-1);
				bw.write("insert into "+getTableName(sfile)+"("+title+") values(" +tLine +");");
				bw.write("\r\n");
			}
		}
		bw.close();
		iw.close();
		out.close();
		br.close();
		ir.close();
		in.close();
	}
	
	public static String getTableName(File file){
		return file.getName().split("\\.")[0];
	}
	
	public static boolean isNumber(String str){
		return StringUtils.isNumeric(str);
	}

}

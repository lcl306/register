package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CodeChange {
	
	public static String JAN_CODE = "Shift_JIS";
	public static String CN_CODE = "GBK";
	public static String STD_CODE = "utf-8";
	public static String ALL_FILE = ".*";
	
	////////////////////////////////////////////////////////////////////////
	
	public static String SRC_CODE = CN_CODE;
	
	public static String TAG_CODE = STD_CODE;
	
	public static final String[] EXCLUDE_FILES = {};
	
	
	public static void main(String[] args)throws Exception{
		/*change(new File("C:/daifuku/wms/sqlscript/oracle/3.initdata/INIT_DCBase.sql"),
				new File("C:/daifuku/wms/sqlscript/oracle/3.initdata/UTF8_INIT_DCBase.sql"));*/
		changeDir("C:/daifuku/wms/sqlscript/oracle/2.table/1.table/wms",
				"C:/daifuku/wms/sqlscript/oracle/2.table/1.table/wmsTmp", ALL_FILE);
		/*changeDir("C:/daifuku/wms/sqlscript/oracle/2.table",
				"C:/daifuku/wms/sqlscript/oracle/2.tableTemp", ALL_FILE);*/
	}
	
	public static void changeDir(String sourceDir, String targetDir, final String fileType)throws Exception{
		changeDir(new File(sourceDir), new File(targetDir), fileType, SRC_CODE, TAG_CODE);
	}
	
	public static void changeDir(File sourceDir, File targetDir, final String fileType, 
			String srcCode, String targetCode)throws Exception{
		if(sourceDir.isDirectory() && sourceDir.exists()){
			if(!targetDir.exists()) targetDir.mkdir();
			File[] files = sourceDir.listFiles(new FilenameFilter(){
				@Override
				public boolean accept(File dir, String name) {
					if(ALL_FILE.equals(fileType)) return true;
					return name.endsWith(fileType);
				}
			});
			for(File f : files){
				if(!isExcludeFile(f)){
					if(f.isDirectory()){
						String tfile = targetDir.getPath()+f.getPath().substring(sourceDir.getPath().length());
						System.out.println(tfile);
						changeDir(f, new File(tfile), fileType, srcCode, targetCode);
					}else{
						change(f, srcCode, new File(targetDir.getPath()+File.separator+f.getName()), targetCode);
					}
				}
			}
		}
	}
	
	public static void change(File source, File target)throws Exception{
		change(source, JAN_CODE, target, STD_CODE);
	}
	
	public static void change(File source, String srcCode, File target, String targetCode)
	throws Exception{
		InputStream in = new FileInputStream(source);
		InputStreamReader ir = new InputStreamReader(in, srcCode);
		BufferedReader br = new BufferedReader(ir);
		OutputStream out = new FileOutputStream(target);
		OutputStreamWriter iw = new OutputStreamWriter(out, targetCode); 
		BufferedWriter bw = new BufferedWriter(iw);
		String line = "";
		while((line=br.readLine())!=null){
			bw.write(line);
			bw.write("\r\n");
		}
		bw.close();
		iw.close();
		out.close();
		br.close();
		ir.close();
		in.close();
	}
	
	public static boolean isExcludeFile(File file){
		for(String excludeF : EXCLUDE_FILES){
			if(file!=null && file.getName().equals(excludeF)) return true;
		}
		return false;
	}

}

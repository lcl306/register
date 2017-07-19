package file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CompareFile {
	
	public static final String SOURCE_PREFIX = "C:/TEMP/WareNavi_3.5/Source/daifuku/wms/tomcat/webapps/";
	
	public static final String TARGET_PREFIX = "C:/TEMP/kaineng/";
	
	public static final String RESULT_DIR = "C:/TEMP/";
	
	public static void main(String[] args) throws IOException{
		/*File file1 = new File("C:/TEMP/kaineng/wms/WEB-INF/src/jp/co/daifuku/common/NotFoundException.java");
		File file2 = new File("C:/TEMP/WareNavi_3.5/Source/daifuku/wms/tomcat/webapps/wms/WEB-INF/src/jp/co/daifuku/common/NotFoundException.java");
		if(!same(file1, file2)){
			System.out.println(file2.getName());
		}*/
		printDiffFile();
	}
	
	public static final String[] WEBINFO_INCLUDE_DIRS = {"control","ini","src","tool","busitune","busitune4rft"};
	
	public static final String[] WMS_INCLUDE_DIRS = {"css","jscript","jsp","report"};
	
	public static void printDiffFile()throws IOException{
		File dirS = new File(SOURCE_PREFIX+"wms/WEB-INF");
		File dirT = new File(TARGET_PREFIX+"wms/WEB-INF");
		File result = new File(RESULT_DIR+"WEB-INF_diff.txt");
		printDiffFile(dirS,dirT,WEBINFO_INCLUDE_DIRS,result);
		
		dirS = new File(SOURCE_PREFIX+"wms");
		dirT = new File(TARGET_PREFIX+"wms");
		result = new File(RESULT_DIR+"WMS_diff.txt");
		printDiffFile(dirS,dirT,WMS_INCLUDE_DIRS,result);
	}
	
	/**
	 * 将dirT和dirS的文件做比较，将dirT不同于dirS的文件写入result中
	 * */
	public static void printDiffFile(File dirS, File dirT, String[] includeDirs, File result) throws IOException{
		CompareFileUtil cf = new CompareFileUtil();
		cf.includeDirs = includeDirs;
		cf.sourcePrefix = SOURCE_PREFIX;
		cf.targetPrefix = TARGET_PREFIX;
		CompareFileUtil.compareFiles(dirS, dirT, cf);
		FileOutputStream out = new FileOutputStream(result);
		OutputStreamWriter or = new OutputStreamWriter(out);
		BufferedWriter bw = new BufferedWriter(or);
		bw.write("-----------------------change file--------------------\r\n");
		for(String name : cf.diffFiles.keySet()){
			bw.write(name+"\r\n");
		}
		bw.write("-----------------------added file--------------------\r\n");
		for(String name : cf.addedFiles.keySet()){
			bw.write(name+"\r\n");
		}
		bw.close();
		or.close();
		out.close();
	}
	
	

}

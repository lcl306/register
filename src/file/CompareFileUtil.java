package file;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CompareFileUtil {
	
	public String sourcePrefix;
	
	public String targetPrefix;
	
	public String[] includeDirs;
	
	public static final String[] EXCLUDE_FILES = {".svn"};
	
	/**
	 * 将dirT和dirS的文件做比较，将dirT不同于dirS的文件写入CompareFile的diffFiles和diffFiles中。
	 * */
	public static CompareFileUtil compareFiles(File dirS, File dirT, CompareFileUtil cf){
		cf.getStandardFiles(dirS, cf.sourcePrefix);
		cf.compareFiles(dirT);
		cf.fileNames.clear();
		return cf;
	}
	
	/**
	 * 将dirT和dirS的文件做比较，复制dirT不存在的文件或dirT的旧文件。
	 * */
	public static CompareFileUtil copyFiles(File dirS, File dirT, boolean copyFlag){
		CompareFileUtil cf = new CompareFileUtil();
		cf.getStandardFiles(dirT, dirT.getPath()+File.separator);
		cf.copyFiles(dirS, dirS.getPath()+File.separator, dirT.getPath()+File.separator, copyFlag);
		cf.fileNames.clear();
		return cf;
	}
	
	private void copyFiles(File dirS, String sourcePath, String targetPath, boolean copyFlag){
		if(dirS.isDirectory() && dirS.exists()){
			File[] files = dirS.listFiles();
 			for(File f : files){
				if(f.exists() && !isExcludeFile(f)){
					if(f.isDirectory() && isIncludeDir(f)){
						copyFiles(f, sourcePath, targetPath, copyFlag);
					}else if(!f.isDirectory()){
						String srcFileName = getFileName(f, sourcePath);
						File t = fileNames.get(srcFileName);
						if(t==null){
							addedFiles.put(srcFileName, f);
							if(copyFlag) FileCopyUtil.copy(f, new File(targetPath+srcFileName));
						}else if(isUpdateFile(f, t)){
							diffFiles.put(srcFileName, f);
							if(copyFlag){
								t.delete();
								FileCopyUtil.copy(f, new File(targetPath+srcFileName));
							}
						}
					}
				}
			}
		}
	}
	
	private void compareFiles(File dirT){
		if(dirT.isDirectory() && dirT.exists()){
			File[] files = dirT.listFiles();
			for(File f : files){
				if(f.exists() && !isExcludeFile(f)){
					if(f.isDirectory() && isIncludeDir(f)){
						compareFiles(f);
					}else if(!f.isDirectory()){
						File s = fileNames.get(getFileName(f, targetPrefix));
						if(s==null){
							addedFiles.put(getFileName(f, targetPrefix), f);
						}else if(!same(s, f)){
							diffFiles.put(getFileName(f, targetPrefix), f);
						}
					}
				}
			}
		}
	}
	
	private static String getFileName(File f, String cut){
		return f.getPath().substring(cut.length()-1);
	}
	
	Map<String, File> diffFiles = new TreeMap<>();
	
	Map<String, File> fileNames = new HashMap<>();
	
	Map<String, File> addedFiles = new TreeMap<>();
	
	private void getStandardFiles(File dirS, String prefix){
		if(dirS.isDirectory() && dirS.exists()){
			File[] files = dirS.listFiles();
			for(File f : files){
				if(f.exists() && !isExcludeFile(f)){
					if(f.isDirectory() && isIncludeDir(f)){
						getStandardFiles(f, prefix);
					}else if(!f.isDirectory()){
						fileNames.put(getFileName(f, prefix), f);
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
	
	private boolean isIncludeDir(File dir){
		if(includeDirs!=null && includeDirs.length>0){
			for(String ir : includeDirs){
				if(dir!=null && dir.getPath().indexOf(File.separator+ir)!=-1) return true;
			}
			return false;
		}
		return true;
	}
	
	public static boolean same(File file1, File file2){
		String file1MD5 = getFileMD5(file1);
		if(file1MD5==null) return false;
		return file1MD5.equals(getFileMD5(file2));
	}
	
	public static boolean isUpdateFile(File src, File target){
		return src.lastModified() > target.lastModified();
	}
	
	/**
	 * 计算文件的MD5
	 * */
	public static String getFileMD5(File file) {
	    if (file==null || !file.isFile()) {
	        return null;
	    }
	    MessageDigest digest = null;
	    FileInputStream in = null;
	    byte buffer[] = new byte[8192];
	    int len;
	    try {
	        digest =MessageDigest.getInstance("MD5");
	        in = new FileInputStream(file);
	        while ((len = in.read(buffer)) != -1) {
	            digest.update(buffer, 0, len);
	        }
	        BigInteger bigInt = new BigInteger(1, digest.digest());
	        return bigInt.toString(16);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    } finally {
	        try {
	            in.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	  
	}

}

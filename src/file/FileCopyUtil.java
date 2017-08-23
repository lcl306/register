package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.Date;

public class FileCopyUtil {
	
	public static void main(String[] args)throws Exception{
		copy(new File("C:/temp/Module1.bas"), 
				new File("C:/temp/Module1.bas.bak"));
		System.out.println(new File("C:/temp/").getPath());
	}
	
	public static final int MAX_BUFF = 20971520;
	
	public static long copy(File src, File target){
        long time=new Date().getTime();
        long length=0;
        try{
	        FileInputStream in=new FileInputStream(src);
	        FileOutputStream out=new FileOutputStream(target);
	        FileChannel inC=in.getChannel();
	        FileChannel outC=out.getChannel();
	        while(true){
	            if(inC.position()==inC.size()){
	                inC.close();
	                outC.close();
	                out.close();
	                in.close();
	                return new Date().getTime()-time;
	            }
	            if((inC.size()-inC.position())<MAX_BUFF)
	                length=(int)(inC.size()-inC.position());
	            else
	                length=MAX_BUFF;
	            inC.transferTo(inC.position(),length,outC);
	            inC.position(inC.position()+length);
	        }
        }catch(Exception e){
        	e.printStackTrace();
        	return 0l;
        }
    }

}

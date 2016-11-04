package image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class PrintImage {
	
	public static final int BUFFER_SIZE = 1024*1024;
	
	public static void print(InputStream in, OutputStream out)throws IOException{
		try{
			int pos = 0;
			byte[] temp = new byte[BUFFER_SIZE];
			while((pos=in.read(temp, 0, BUFFER_SIZE))!=-1){
				out.write(temp, 0, pos);
			}
		}finally{
			try{
				if(in!=null)in.close();
				if(out!=null)out.close();
			}catch(Exception e){}
		}
	}
	
	public static void print(BufferedImage image, OutputStream out)throws IOException{
		try{
		ImageIO.write(image, "JPEG", out);
		}finally{
			if(out!=null)out.close();
		}
	}

}

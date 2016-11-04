package image;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestImage {
	
	public static void main(String[] args) throws Exception{
		Object[] info = RandImage.getImage();
		OutputStream out = new FileOutputStream("D:/长亮欧巴/"+info[1]);
		PrintImage.print((BufferedImage)info[0], out);
	}

}

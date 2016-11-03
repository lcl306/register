package barcode.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import barcode.barcode.BarcodeManager;
import barcode.barcode.SimpleCodeCreator;

public class BarcodeManagerTest {

	public static void main(String[] args) throws Exception{
		Image image = BarcodeManager.createImage(new SimpleCodeCreator("1234567890$-:/.+abcd"));
		ImageIO.write((BufferedImage)image,"BMP", new File("D:\\bar1.jpg"));
	}

}

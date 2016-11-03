package barcode.test;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import barcode.barcode4j.Barcode4jManager;
import barcode.barcode4j.BarcodeCreator;


public class Barcode4jManagerTest {

	public static void main(String[] args)throws Exception {

		Image image = Barcode4jManager.getCodeImage(BarcodeCreator.createNw7Bean("1234567890$-:/.+abcd"));
		ImageIO.write((BufferedImage)image, "BMP", new File("D:\\out3.jpg"));
	}

}

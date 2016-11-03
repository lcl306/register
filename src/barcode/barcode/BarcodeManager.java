package barcode.barcode;

import java.awt.Image;
import java.awt.image.BufferedImage;

import barcode.barcode.util.BarCode;


public class BarcodeManager {

	 public static Image createImage(CodeCreator cb){
		 BufferedImage bufferedimage = null;
		try {
			BarCode barcode1 = cb.createBar();
			bufferedimage = new BufferedImage(
					barcode1.getSize().width, barcode1.getSize().height,BufferedImage.TYPE_BYTE_BINARY);
			java.awt.Graphics2D graphics2d = bufferedimage.createGraphics();
			barcode1.paint(graphics2d);
			barcode1.invalidate();
			graphics2d.dispose();
			return bufferedimage;
		} catch (Exception exception) {
			exception.printStackTrace();
			return bufferedimage;
		}
	 }

}

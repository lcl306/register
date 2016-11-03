package barcode.barcode4j;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

public class Barcode4jManager {

	/**
	 * if barcode bean is got, the method print the java.awt.Image of the barcode
	 * */
	public static Image getCodeImage(BarcodeBean bean){
		BitmapCanvasProvider canvas = new BitmapCanvasProvider( bean.getDpi(), BufferedImage.TYPE_BYTE_BINARY, false, 0);
        bean.getBean().generateBarcode(canvas, bean.getCode());
        BufferedImage image = canvas.getBufferedImage();
        try{if(canvas!=null)canvas.finish();}catch(IOException e){};
        return image;
	}

}

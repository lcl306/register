package barcode.barcode;

import barcode.barcode.util.BarCode;


public class SimpleCodeCreator implements CodeCreator {

	private String code;

	public SimpleCodeCreator(String code){
		this.code = code;
	}

	public BarCode createBar() {
		return BarcodeHelper.createBar(code, "300", "150", "0.7", "10");
	}

}

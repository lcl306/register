package barcode.barcode4j;

import org.krysalis.barcode4j.impl.AbstractBarcodeBean;

public class BarcodeBean {

	private int dpi;

	private String code;

	AbstractBarcodeBean bean;

	public BarcodeBean(AbstractBarcodeBean bean){
		this.bean = bean;
	}

	public AbstractBarcodeBean getBean() {
		return bean;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getDpi() {
		return dpi;
	}

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}



}

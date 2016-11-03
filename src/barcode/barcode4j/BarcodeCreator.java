package barcode.barcode4j;

import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.tools.UnitConv;


/**
 * codabar(NW7) is 4 bar and 3 space, standardized by JIS-X-0506
 * character set is 0-9 a b c d - $ : / . +  "a b c d" is used for starting and ending mark of barcode
 * @author lcl
 * @date 2009-4-3
 * */
public class BarcodeCreator {

	/**
	 * moduleWidth(X) is the width of narrow bar, >0.191mm, so dpi =< 130
	 * wideFactor is important, if <2.2, some bar would be touched, so "cap" must >X-t and <5.3X
	 *   when barcode4j creates baracode, the scale of wide bar's width and narrow bar's width is either 3.0 or 2.0
	 * quietZone is region of barcode, quietZone >10X and >2.54mm
	 * barHeight is height of barcode, must > (length of barcode) * 15% and 5mm
	 * checksumMode is check of code of bar, codabar's check type is ChecksumMode.CP_IGNORE and ChecksumMode.CP_AUTO
	 * @author lcl
	 * @date 2009-4-13
	 * */
	public static BarcodeBean createNw7Bean(int dpi, boolean isFitDpi, Double widthFactor,
			Double barHeight, Double fontSize, String code){
		CodabarBean bean = new CodabarBean();
		if(isFitDpi) bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
		if(widthFactor!=null) bean.setWideFactor(widthFactor);  //3
		//bean.doQuietZone(false);
		if(barHeight!=null) bean.setBarHeight(barHeight);
		if(fontSize!=null) bean.setFontSize(fontSize);
		bean.setFontName("HeiseiKakuGo-W5");
		//bean.setQuietZone(6.35);
		//bean.setChecksumMode(ChecksumMode.CP_AUTO);
		BarcodeBean codeBean = new BarcodeBean(bean);
		codeBean.setCode(code);
		codeBean.setDpi(dpi);
		return codeBean;
	}

	/**
	 * @author lcl
	 * @date 2009-4-13
	 * @see  createNw7Bean(int, boolean, Double, Double, Double, String)
	 * */
	public static BarcodeBean createNw7Bean(Double barHeight, Double fontSize, String code){
		return createNw7Bean(120, true, 3.0, barHeight, fontSize, code);
	}

	/**
	 * @author lcl
	 * @date 2009-4-13
	 * @see  createNw7Bean(int, boolean, Double, Double, Double, String)
	 * */
	public static BarcodeBean createNw7Bean(String code){
		return createNw7Bean(5.0, 2.0, code);
	}

}

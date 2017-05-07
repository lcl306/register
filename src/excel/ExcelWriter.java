package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;

public class ExcelWriter {

	protected String fileName;
	
	protected Workbook workbook;

	/**
	 * 初始化Excel
	 * @param fileName 导出文件名
	 */
	public ExcelWriter(Workbook workbook, String fileName) {
		if(workbook==null){
			workbook = WorkbookFactory.createWorkbook(fileName);
		}
		this.workbook = workbook;
		this.fileName = fileName;
	}
	
	public ExcelWriter(String fileName){
		this(null, fileName);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void exportXLS() throws Exception {
		exportXLS(fileName);
	}
	
	public void exportXLS(OutputStream out) throws Exception {
		try {
			workbook.write(out);
		} catch (IOException e) {
			throw new Exception(" 生成导出Excel文件出错! ", e);
		}
	}

	public void exportXLS(String fileName) throws Exception {
		FileOutputStream fOut = new FileOutputStream(fileName);
		exportXLS(fOut);
		fOut.flush();
		fOut.close();
	}

}

package excel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookFactory {
	
	static Workbook createWorkbook(String fileName){
		String excelType = getExcelType(fileName);
		if(excelType.equals("xlsx") || excelType.equals("xlsm"))
			return new XSSFWorkbook();
		else
			return new HSSFWorkbook();
	}
	
	static Workbook createWorkbook(InputStream in, String excelType) throws IOException{
		if(excelType.equals("xlsx") || excelType.equals("xlsm"))
			return new XSSFWorkbook(in);
		else
			return new HSSFWorkbook(new POIFSFileSystem(in)); 
	}
	
	static String getExcelType(String fileName){
		return fileName.substring(fileName.lastIndexOf(".")+1);
	}

}

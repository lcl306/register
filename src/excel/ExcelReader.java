package excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelReader {

	    private Workbook wb;    
	    
	    public ExcelReader(){}
	    
	    /**
	     * 加载Excel
	     */
	    protected void initExcel(InputStream is, String excelType){
	    	try {
	            wb = WorkbookFactory.createWorkbook(is, excelType);
	        } catch (IOException e) {    
	            e.printStackTrace();    
	        }
	    	
	    }
	    
	    public void readExcel(String fileName){
	    	FileInputStream in=null;
			try {
				in = new FileInputStream(fileName);
				this.initExcel(in, WorkbookFactory.getExcelType(fileName));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	    }
	    
	    public Sheet getSheet(String sheetName){
	    	Sheet sheet = null;
	    	if(sheetName==null || sheetName.trim().length()==0)
	    		sheet = wb.getSheetAt(0);
	    	else
	    		sheet = wb.getSheet(sheetName);
	    	return sheet;
	    }
	    
	    public Workbook getWorkbook(){
	    	return wb;
	    }
	    

}

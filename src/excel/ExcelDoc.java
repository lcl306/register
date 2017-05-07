package excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelDoc {
	
	public static Cell readCell(Sheet sheet, String coordinate){
    	int[] rowCol=ExcelUtil.getCellCoordinate(coordinate);
    	int rowIdx=rowCol[1];
    	int colIdx=rowCol[0];
    	return readCell(sheet, rowIdx, colIdx);
    }
    
    public static Cell readCell(Sheet sheet, int rowIdx,int colIdx){
    	Row rowCon = sheet.getRow(rowIdx); 
    	if(rowCon==null) return null;
    	int totColNum = rowCon.getPhysicalNumberOfCells();
    	if(colIdx>totColNum) return null;
    	return rowCon.getCell(colIdx);
    }
    
    public static void setColor(Workbook workbook, Cell cell, short color){
		if(cell!=null){
			CellStyle style = workbook.createCellStyle();
			style.cloneStyleFrom(cell.getCellStyle());
			style.setFillPattern(CellStyle.BIG_SPOTS);
			style.setFillForegroundColor(color);
			style.setFillBackgroundColor(IndexedColors.WHITE.getIndex());
			//style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			//style.setFillForegroundColor(color);
			cell.setCellStyle(style);
		}
	}

}

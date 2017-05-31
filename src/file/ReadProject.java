package file;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import util.date.HolidayUtil;
import excel.ExcelDoc;
import excel.ExcelReader;
import excel.ExcelUtil;
import excel.ExcelWriter;

public class ReadProject {
	
	private String readFileName = "C:/文档/大福/科尔沁/科尔沁_开发工程表.xlsx";
	
	private String writeFileName = "C:/文档/大福/科尔沁/科尔沁_开发工程表write.xlsx";
	
	private String sheetName = "工程";
	
	private String startCellName = "P6";
	
	private String endCellName = "HU6";
	
	private int endRow = 45;
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	private Workbook workbook;

	private Sheet sheet;
	
	private List<Cell> dateCells = new ArrayList<>();
	
	private boolean right = true;
	
	//private short color = IndexedColors.LEMON_CHIFFON.getIndex();
	private short color = IndexedColors.LIGHT_GREEN.getIndex();
	
	//private short void_color = IndexedColors.WHITE.getIndex();
	
	public ReadProject(){
		initSheet();
		initCell();
	}
	
	public void initSheet(){
		ExcelReader reader = new ExcelReader();
		reader.readExcel(readFileName);
		sheet = reader.getSheet(sheetName);
		workbook = reader.getWorkbook();
	}
	
	public void initCell(){
		int[] starts = ExcelUtil.getCellCoordinate(startCellName);
		int[] ends = ExcelUtil.getCellCoordinate(endCellName);
		int row = starts[1];
		Date yearMonth = null;
		for(int i=starts[0]; i<=ends[0]; i++){
			Cell cell = ExcelDoc.readCell(sheet, row, i);
			if(cell==null){
				right = false;
			}else{
				Date ym = getYearMonth(cell);
				yearMonth = ym!=null?ym:yearMonth;
				right &= spell(cell, yearMonth);
				dateCells.add(cell);
			}
		}
	}
	
	public static boolean spell(Cell cell, Date yearMonth){
		boolean rtn = true;
		Date day = cell.getDateCellValue();
		if(day!=null && yearMonth!=null){
			Calendar d = Calendar.getInstance();
			d.setTime(day);
			Calendar ym = Calendar.getInstance();
			ym.setTime(yearMonth);
			ym.set(Calendar.DAY_OF_MONTH, d.get(Calendar.DAY_OF_MONTH));
			cell.setCellValue(ym.getTime());
		}else{
			rtn = false;
		}
		return rtn;
	}
	
	public Date getYearMonth(Cell cell){
		int row = cell.getRowIndex()-1;
		int col = cell.getColumnIndex();
		Cell c = ExcelDoc.readCell(sheet, row, col);
		return c.getDateCellValue();
	}
	
	public void fill(){
		if(right){
			for(Cell cell : dateCells){
				fillWeek(cell);
				fillColor(cell);
			}
		}
	}
	
	public void fillWeek(Cell cell){
		int row = cell.getRowIndex()+1;
		int col = cell.getColumnIndex();
		Cell c = ExcelDoc.readCell(sheet, row, col);
		c.setCellValue(HolidayUtil.getWeekName(cell.getDateCellValue()));
	}
	
	public void fillColor(Cell cell){
		int row = cell.getRowIndex();
		int col = cell.getColumnIndex();
		/*for(int i=row; i<endRow; i++){
			Cell c = ExcelDoc.readCell(sheet, i, col);
			ExcelDoc.setColor(workbook, c, void_color);
		}*/
		Date date = cell.getDateCellValue();
		int w = HolidayUtil.getWeek(date);
		if(w==0 || w==6 || HolidayUtil.isHoliday(date)){
			for(int i=row; i<endRow; i++){
				Cell c = ExcelDoc.readCell(sheet, i, col);
				ExcelDoc.setColor(workbook, c, color);
			}
		}
	}
	
	public void write(){
		if(right){
			ExcelWriter writer = new ExcelWriter(workbook, writeFileName);
			try {
				writer.exportXLS();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ReadProject r = new ReadProject();
		r.fill();
		r.write();
	}

}

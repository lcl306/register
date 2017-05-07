package excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelUtil {

	final static String[] CHAR_COLS = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "AA", "AB", "AC", "AD", "AE", "AF", "AG",
			"AH", "AI", "AJ", "AK", "AL", "AM", "AN", "AO", "AP", "AQ", "AR",
			"AS", "AT", "AU", "AV", "AW", "AX", "AY", "AZ", "BA", "BB", "BC",
			"BD", "BE", "BF", "BG", "BH", "BI", "BJ", "BK", "BL", "BM", "BN",
			"BO", "BP", "BQ", "BR", "BS", "BT", "BU", "BV", "BW", "BX", "BY",
			"BZ", "CA", "CB", "CC", "CD", "CE", "CF", "CG", "CH", "CI", "CJ",
			"CK", "CL", "CM", "CN", "CO", "CP", "CQ", "CR", "CS", "CT", "CU",
			"CV", "CW", "CX", "CY", "CZ", "DA", "DB", "DC", "DD", "DE", "DF",
			"DG", "DH", "DI", "DJ", "DK", "DL", "DM", "DN", "DO", "DP", "DQ",
			"DR", "DS", "DT", "DU", "DV", "DW", "DX", "DY", "DZ", "EA", "EB",
			"EC", "ED", "EE", "EF", "EG", "EH", "EI", "EJ", "EK", "EL", "EM",
			"EN", "EO", "EP", "EQ", "ER", "ES", "ET", "EU", "EV", "EW", "EX",
			"EY", "EZ", "FA", "FB", "FC", "FD", "FE", "FF", "FG", "FH", "FI",
			"FJ", "FK", "FL", "FM", "FN", "FO", "FP", "FQ", "FR", "FS", "FT",
			"FU", "FV", "FW", "FX", "FY", "FZ", "GA", "GB", "GC", "GD", "GE",
			"GF", "GG", "GH", "GI", "GJ", "GK", "GL", "GM", "GN", "GO", "GP",
			"GQ", "GR", "GS", "GT", "GU", "GV", "GW", "GX", "GY", "GZ", "HA",
			"HB", "HC", "HD", "HE", "HF", "HG", "HH", "HI", "HJ", "HK", "HL",
			"HM", "HN", "HO", "HP", "HQ", "HR", "HS", "HT", "HU", "HV", "HW",
			"HX", "HY", "HZ", "IA", "IB", "IC", "ID", "IE", "IF", "IG", "IH",
			"II", "IJ", "IK", "IL", "IM", "IN", "IO", "IP", "IQ", "IR", "IS",
			"IT", "IU", "IV", "IW", "IX", "IY", "IZ", "JA", "JB", "JC", "JD",
			"JE", "JF", "JG", "JH", "JI", "JJ", "JK", "JL", "JM", "JN", "JO",
			"JP", "JQ", "JR", "JS", "JT", "JU", "JV", "JW", "JX", "JY", "JZ",
			"KA", "KB", "KC", "KD", "KE", "KF", "KG", "KH", "KI", "KJ", "KK",
			"KL", "KM", "KN", "KO", "KP", "KQ", "KR", "KS", "KT", "KU", "KV",
			"KW", "KX", "KY", "KZ" };

	public static boolean hasMerged(int row, int col, Sheet sheet) {
		/*int num = sheet.getNumMergedRegions();
		for (int i = 0; i < num; i++) {
			CellRangeAddress ra = sheet.getMergedRegion(i);
			if(ra.getFirstColumn()<=col && ra.getLastColumn()>=col && ra.getFirstRow()<=row && ra.getLastRow()>=row){
				return true;
			}
		}*/
		int num = sheet.getNumMergedRegions();
		if(num>0){
			for(CellRangeAddress ra : sheet.getMergedRegions()){
				if(ra.getFirstColumn()<=col && ra.getLastColumn()>=col && ra.getFirstRow()<=row && ra.getLastRow()>=row){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 该方法用于实现合并单元格 cell表示起始单元格，cols,rows表示合并几列和几行
	 * 
	 * @param cell
	 * @param cols
	 * @param rows
	 */
	public static CellRangeAddress mergedCells(Cell cell, int cols, int rows) {
		int row = cell.getRowIndex();
		int col = cell.getColumnIndex();
		int end_col = (col + cols - 1);
		int end_row = (row + rows - 1);
		// sheet.addMergedRegion(new Region(row, col, row, end_col));//
		// 四个参数分别是：起始行，起始列，结束行，结束列
		// CellRangeAddress（int， int， int， int） 参数：起始行号，终止行号， 起始列号，终止列号
		CellRangeAddress range = new CellRangeAddress(row, end_row, col,end_col);// 四个参数分别是：起始行，结束行，起始列，结束列
		cell.getSheet().addMergedRegion(range);
		return range;
	}

	/**
	 * 根据横纵坐标读取单元格的内容 横纵坐标规范字母在前数字在后例如（ A1,H2） 返回一个数组，两个数字值，第一个值是第几行，第二个值是第几列
	 */
	public static int[] getCellCoordinate(String rowCol) {
		rowCol = rowCol.toUpperCase();
		char[] chars = rowCol.toCharArray();
		String colChar = "";
		String rowChar = "";
		for (char c : chars) {
			if (c >= 'A' && c <= 'Z')
				colChar += c;
			else
				rowChar += c;
		}
		int colNum = getColNum(colChar);
		int rowNum = Integer.valueOf(rowChar);
		int[] ints = { colNum, --rowNum };
		return ints;
	}

	/**
	 * 把横坐标字母转换成数字
	 */
	public static int getColNum(String colChar) {
		// "AA","AB","AC","AD","AE","AF","AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"
		for (int i = 0; i < CHAR_COLS.length; i++) {
			if (colChar.equalsIgnoreCase(CHAR_COLS[i])) {
				return i;
			}
		}
		return -1;
	}

	public static String getCol(int pos) {
		return CHAR_COLS[pos];
	}
}

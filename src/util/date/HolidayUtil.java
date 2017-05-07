package util.date;

import java.util.Calendar;
import java.util.Date;

public class HolidayUtil {
	
	private static String[] weeks = {"日","一","二","三","四","五","六"};
	
	private static String[] solarMds = {"0101", "0405","0501","1001","1002","1003","1004","1005", "1006", "1007"};
	
	private static String[] lunarMds = {"0101", "0102","0103","0104","0105","0106","0505","0815", "1230"};
	
	public static int getWeek(Date day){
		Calendar d = Calendar.getInstance();
		d.setTime(day);
		return d.get(Calendar.DAY_OF_WEEK)-1;
	}
	
	public static String getWeekName(Date day){
		return weeks[getWeek(day)];
	}
	
	public static boolean isHoliday(Date da){
		Calendar date = Calendar.getInstance();
		date.setTime(da);
		Lunar ld = new Lunar(date);
		int month = date.get(Calendar.MONTH)+1;
		int day = date.get(Calendar.DAY_OF_MONTH);
		String smd = (month<10?"0"+month:month)+""+(day<10?"0"+day:day);
		String lmd = ld.getYyyyMMdd().substring(4);
		if(solarMds!=null){
			for(String md : solarMds){
				if(md.equals(smd)){
					return true;
				} 
			}
		}
		if(lunarMds!=null){
			for(String md : lunarMds){
				if(md.equals(lmd)){
					return true;
				}
			}
		}
		return false;
	}

}

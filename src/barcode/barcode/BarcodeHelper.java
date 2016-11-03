package barcode.barcode;

import java.awt.Color;
import java.awt.Font;
import java.util.StringTokenizer;

import barcode.barcode.util.BarCode;


public class BarcodeHelper {

	public static BarCode createBar(String code, String width, String height, String barHeightCM, String fontSize){
		 BarCode barcode = new BarCode();
	        try{
	            BarcodeHelper.setParameter(barcode, "barType", "CODABAR");
	            if(width!=null && height!=null){
	            	BarcodeHelper.setParameter(barcode, "width", width);
	            	BarcodeHelper.setParameter(barcode, "height", height);
	            	BarcodeHelper.setParameter(barcode, "autoSize", "n");
	            }
	            barcode.setSize(barcode.width, barcode.height);
	            BarcodeHelper.setParameter(barcode, "code", code);
	            BarcodeHelper.setParameter(barcode, "st", "y");
	            if(fontSize!=null) BarcodeHelper.setParameter(barcode, "textFont", "Arial|PLAIN|" +fontSize);
	            if(barHeightCM!=null) BarcodeHelper.setParameter(barcode,"barHeightCM", barHeightCM);
	            BarcodeHelper.setParameter(barcode, "leftMarginCM", "0.1");
	            BarcodeHelper.setParameter(barcode, "topMarginCM", "0.1");
	            BarcodeHelper.setParameter(barcode, "checkCharacter", "n");
	            BarcodeHelper.setParameter(barcode, "checkCharacterInText", "n");
	        }catch(Exception exception){
	            exception.printStackTrace();
	            barcode.code = "Parameter Error";
	        }
	        return barcode;
	    }

	 private static void setParameter(BarCode barcode, String s, String s1){
	        if(s1 != null){
	            if(s.equals("code"))
	                barcode.code = s1;
	            else
	            if(s.equals("width"))
	                barcode.width = (new Integer(s1)).intValue();
	            else
	            if(s.equals("height"))
	                barcode.height = (new Integer(s1)).intValue();
	            else
	            if(s.equals("autoSize"))
	                barcode.autoSize = s1.equalsIgnoreCase("y");
	            else
	            if(s.equals("st"))
	                barcode.showText = s1.equalsIgnoreCase("y");
	            else
	            if(s.equals("textFont"))
	                barcode.textFont = convertFont(s1);
	            else
	            if(s.equals("fontColor"))
	                barcode.fontColor = convertColor(s1);
	            else
	            if(s.equals("barColor"))
	                barcode.barColor = convertColor(s1);
	            else
	            if(s.equals("backColor"))
	                barcode.backColor = convertColor(s1);
	            else
	            if(s.equals("rotate"))
	                barcode.rotate = (new Integer(s1)).intValue();
	            else
	            if(s.equals("barHeightCM"))
	                barcode.barHeightCM = (new Double(s1)).doubleValue();
	            else
	            if(s.equals("x"))
	                barcode.X = (new Double(s1)).doubleValue();
	            else
	            if(s.equals("n"))
	                barcode.N = (new Double(s1)).doubleValue();
	            else
	            if(s.equals("leftMarginCM"))
	                barcode.leftMarginCM = (new Double(s1)).doubleValue();
	            else
	            if(s.equals("topMarginCM"))
	                barcode.topMarginCM = (new Double(s1)).doubleValue();
	            else
	            if(s.equals("checkCharacter"))
	                barcode.checkCharacter = s1.equalsIgnoreCase("y");
	            else
	            if(s.equals("checkCharacterInText"))
	                barcode.checkCharacterInText = s1.equalsIgnoreCase("y");
	            else
	            if(s.equals("Code128Set"))
	                barcode.Code128Set = s1.charAt(0);
	            else
	            if(s.equals("UPCESytem"))
	                barcode.UPCESytem = s1.charAt(0);
	            else
	            if(s.equals("barType")){
	                if(s1.equalsIgnoreCase("CODE39"))
	                    barcode.barType = 0;
	                else
	                if(s1.equalsIgnoreCase("CODE39EXT"))
	                    barcode.barType = 1;
	                else
	                if(s1.equalsIgnoreCase("INTERLEAVED25"))
	                    barcode.barType = 2;
	                else
	                if(s1.equalsIgnoreCase("CODE11"))
	                    barcode.barType = 3;
	                else
	                if(s1.equalsIgnoreCase("CODABAR"))
	                    barcode.barType = 4;
	                else
	                if(s1.equalsIgnoreCase("MSI"))
	                    barcode.barType = 5;
	                else
	                if(s1.equalsIgnoreCase("UPCA"))
	                    barcode.barType = 6;
	                else
	                if(s1.equalsIgnoreCase("IND25"))
	                    barcode.barType = 7;
	                else
	                if(s1.equalsIgnoreCase("MAT25"))
	                    barcode.barType = 8;
	                else
	                if(s1.equalsIgnoreCase("CODE93"))
	                    barcode.barType = 9;
	                else
	                if(s1.equalsIgnoreCase("EAN13"))
	                    barcode.barType = 10;
	                else
	                if(s1.equalsIgnoreCase("EAN8"))
	                    barcode.barType = 11;
	                else
	                if(s1.equalsIgnoreCase("UPCE"))
	                    barcode.barType = 12;
	                else
	                if(s1.equalsIgnoreCase("CODE128"))
	                    barcode.barType = 13;
	                else
	                if(s1.equalsIgnoreCase("CODE93EXT"))
	                    barcode.barType = 14;
	                else
	                if(s1.equalsIgnoreCase("POSTNET"))
	                    barcode.barType = 15;
	                else
	                if(s1.equalsIgnoreCase("PLANET"))
	                    barcode.barType = 16;
	                else
	                if(s1.equalsIgnoreCase("UCC128"))
	                    barcode.barType = 17;
	            }
	        }
	    }

	    private static Font convertFont(String s)
	    {
	        StringTokenizer stringtokenizer = new StringTokenizer(s, "|");
	        String s1 = stringtokenizer.nextToken();
	        String s2 = stringtokenizer.nextToken();
	        String s3 = stringtokenizer.nextToken();
	        byte byte0 = -1;
	        if(s2.trim().toUpperCase().equals("PLAIN"))
	            byte0 = 0;
	        else
	        if(s2.trim().toUpperCase().equals("BOLD"))
	            byte0 = 1;
	        else
	        if(s2.trim().toUpperCase().equals("ITALIC"))
	            byte0 = 2;
	        return new Font(s1, byte0, (new Integer(s3)).intValue());
	    }

	    private static Color convertColor(String s)
	    {
	        Color color = null;
	        if(s.trim().toUpperCase().equals("RED"))
	            color = Color.red;
	        else
	        if(s.trim().toUpperCase().equals("BLACK"))
	            color = Color.black;
	        else
	        if(s.trim().toUpperCase().equals("BLUE"))
	            color = Color.blue;
	        else
	        if(s.trim().toUpperCase().equals("CYAN"))
	            color = Color.cyan;
	        else
	        if(s.trim().toUpperCase().equals("DARKGRAY"))
	            color = Color.darkGray;
	        else
	        if(s.trim().toUpperCase().equals("GRAY"))
	            color = Color.gray;
	        else
	        if(s.trim().toUpperCase().equals("GREEN"))
	            color = Color.green;
	        else
	        if(s.trim().toUpperCase().equals("LIGHTGRAY"))
	            color = Color.lightGray;
	        else
	        if(s.trim().toUpperCase().equals("MAGENTA"))
	            color = Color.magenta;
	        else
	        if(s.trim().toUpperCase().equals("ORANGE"))
	            color = Color.orange;
	        else
	        if(s.trim().toUpperCase().equals("PINK"))
	            color = Color.pink;
	        else
	        if(s.trim().toUpperCase().equals("WHITE"))
	            color = Color.white;
	        else
	        if(s.trim().toUpperCase().equals("YELLOW"))
	            color = Color.yellow;
	        return color;
	    }

}

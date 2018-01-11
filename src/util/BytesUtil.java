package util;

import java.math.BigInteger;

public class BytesUtil {
	
	public static byte[] numToByte(Number num){
		if(num instanceof Long){
			return longToByte((Long)num);
		}else if(num instanceof Integer){
			return intToByte((Integer)num);
		}else if(num instanceof Short){
			return shortToByte((Short)num);
		}else if(num instanceof Byte){
			return new byte[]{(Byte)num};
		}
		return null;
	}
	
	public static Number byteToNum(byte[] b){
		switch (b.length) {
		case 8:
			return byteToLong(b);
		case 4:
			return byteToInt(b);
		case 2:
			return byteToShort(b);
		case 1:
			return b[0];
		default:
			return null;
		}
	}
	
	public static byte[] longToByte(long number) { 
        long temp = number; 
        byte[] b = new byte[8]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位 
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
    
    //byte数组转成long 
    public static long byteToLong(byte[] b) { 
        long s = 0; 
        long s0 = b[0] & 0xff;// 最低位 
        long s1 = b[1] & 0xff; 
        long s2 = b[2] & 0xff; 
        long s3 = b[3] & 0xff; 
        long s4 = b[4] & 0xff;// 最低位 
        long s5 = b[5] & 0xff; 
        long s6 = b[6] & 0xff; 
        long s7 = b[7] & 0xff; 
 
        // s0不变 
        s1 <<= 8; 
        s2 <<= 16; 
        s3 <<= 24; 
        s4 <<= 8 * 4; 
        s5 <<= 8 * 5; 
        s6 <<= 8 * 6; 
        s7 <<= 8 * 7; 
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7; 
        return s; 
    } 
 
 
    public static byte[] intToByte(int number) { 
        int temp = number; 
        byte[] b = new byte[4]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue();// 将最低位保存在最低位 
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
 
     
    public static int byteToInt(byte[] b) { 
        int s = 0; 
        int s0 = b[0] & 0xff;// 最低位 
        int s1 = b[1] & 0xff; 
        int s2 = b[2] & 0xff; 
        int s3 = b[3] & 0xff; 
        s3 <<= 24; 
        s2 <<= 16; 
        s1 <<= 8; 
        s = s0 | s1 | s2 | s3; 
        return s; 
    } 
 
     
    public static byte[] shortToByte(short number) { 
        int temp = number; 
        byte[] b = new byte[2]; 
        for (int i = 0; i < b.length; i++) { 
            b[i] = new Integer(temp & 0xff).byteValue(); // 将最低位保存在最低位 
            temp = temp >> 8; // 向右移8位 
        } 
        return b; 
    } 
 
     
    public static short byteToShort(byte[] b) { 
        short s = 0; 
        short s0 = (short) (b[0] & 0xff);// 最低位 
        short s1 = (short) (b[1] & 0xff); 
        s1 <<= 8; 
        s = (short) (s0 | s1); 
        return s; 
    }
    
    /**
     * 将byte[]转化为以string形式表示的进制数
     * @param radix：2：二进制的string表示   16：16进制的string表示
     * */
    public static String byteToBit(byte[] bytes, int radix){  
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
    }
    
    /**
     * 将byte[]解析为string形式的二进制
     * 功能类似Integer.toBinaryString(int)
     * 如果对于一个byte流，每个byte想解析成二进制，可以使用该方法，java默认每个byte以十进制表示
     * */
    public static String byteToBit(byte[] bytes){  
        return byteToBit(bytes, 2);
    }

}

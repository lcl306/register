package clsgen.info;

public interface FieldInfo {
	
	public static final int SET_METHOD = 1;
	
	public static final int GET_METHOD = 2;
	
	public static final int SET_GET_METHOD = 3;
	
	public String getType();
	
	public String getName();

}

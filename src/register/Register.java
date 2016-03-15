package register;


public class Register {
	
	static String encrySeed = "6g402-39gj-53$9b9sj6";
	
	public static void main(String[] args) throws Exception{
		EncryptUtil eu = new EncryptUtil(encrySeed);
		String code = eu.encrypt("20200601");
		System.out.println(code);
		System.out.println(eu.decrypt(code));
		code = eu.encrypt("20250601");
		System.out.println(code);
		System.out.println(eu.decrypt(code));
	}

}

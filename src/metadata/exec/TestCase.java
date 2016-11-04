package metadata.exec;

public class TestCase {
	
	@Code(code="123456")
	public String field1 = "";
	
	@Test
	public void method1(){
		System.out.println(ExecuteTestCase.getCode(TestCase.class, "field1"));
	}
	
	public void method2(){
		System.out.println("method2");
	}
	
	public static void main(String[] args)throws Exception{
		ExecuteTestCase.test(TestCase.class);
	}

}

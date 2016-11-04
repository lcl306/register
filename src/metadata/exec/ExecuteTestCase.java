package metadata.exec;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ExecuteTestCase {
	
	public static void test(Class clazz)
	throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object object = clazz.newInstance();
		Method[] methods = clazz.getMethods();
		for(Method m : methods){
			Class<Test> test = Test.class;
			//如果方法前加上@Test就执行
			if(m.isAnnotationPresent(test)){
				m.invoke(object, new Object[]{});
			}
		}
	}
	
	public static String getCode(Class clazz, String fieldName){
		String code = "";
		try{
			code = clazz.getField(fieldName).getAnnotation(Code.class).code();
		}catch(Exception e){
			
		}
		return code;
	}

}

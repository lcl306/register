package clsgen.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import clsgen.Bean;
import clsgen.ClassGenerator;
import clsgen.info.ClassInfo;
import clsgen.info.FieldInfo;


public class ClassGeneratorTest{

	ClassInfo clf = null;
	
	FieldInfo[] finfos = null;

	@Before
	public void setUp() throws Exception {
		clf = new ClassInfo(){
			public String getClassName() {
				return "clsgen.test.TestBean";
			}
			public String getExtendsClass() {
				return "java.lang.Object";
			}
			public String[] getInterfaces() {
				return new String[]{"clsgen.Bean"};
			}
		};
		FieldInfo finfo = new FieldInfo(){
			public String getType() {
				return "java.lang.String";
			}
			public String getName() {
				return "name";
			}
		};
		FieldInfo finfo2 = new FieldInfo(){
			public String getType() {
				return "java.lang.Integer";
			}
			public String getName() {
				return "name2";
			}
		};
		finfos = new FieldInfo[2];
		finfos[0] = finfo;
		finfos[1] = finfo2;
	}

	@After
	public void tearDown() throws Exception {
		
	}

	/*
	 * Test method for 'bcel.ClassGenerator.generate(ClassInfo, FieldInfo[])'
	 */
	@Test
	public void testGenerate() throws Exception{
//		Class clazz = Class.forName("bcel.ClassGenerateUtil");
//		Object rtn = clazz.newInstance();
//		Method[] mds = clazz.getDeclaredMethods();
//		for(int i=0; i<mds.length; i++){
//			Method md = mds[i];
//			System.out.println(md.getName());
//		}
		
//		Object rtn = new ClassGenerator().generate(clf, finfos);
//		Class cls = rtn.getClass();
//		Method[] mds = cls.getMethods();
//		for(int i=0; i<mds.length; i++){
//			Method md = mds[i];
//			System.out.println(md.getName());
//		}
		
		Bean rtn = new ClassGenerator().generate2(clf, finfos);
		Class cls = rtn.getClass();
		Field[] fs = cls.getDeclaredFields();
		for(int i=0; i<fs.length; i++){
			Field fd = fs[i];
			System.out.println(fd.getName());
		}
		Method[] mds = cls.getDeclaredMethods();
		for(int i=0; i<mds.length; i++){
			Method md = mds[i];
			System.out.println(md.getName());
		}
		rtn.setName("testname");
		rtn.setName2(123);
		Assert.assertEquals("testname", rtn.getName());
		Assert.assertEquals(new Integer(123), rtn.getName2());
	}

}

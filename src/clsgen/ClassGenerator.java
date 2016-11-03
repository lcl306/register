package clsgen;

import clsgen.bcel.ClassGenerateUtil;
import clsgen.info.ClassInfo;
import clsgen.info.FieldInfo;

public class ClassGenerator extends ClassLoader {
	
	public Object generate(ClassInfo clf, FieldInfo[] fieldInfos){
		byte[] data = ClassGenerateUtil.genrateClassData(clf, fieldInfos);
		return generate(data, clf.getClassName());
	}
	
	public Class gererateClass(ClassInfo clf, FieldInfo[] fieldInfos){
		byte[] data = ClassGenerateUtil.genrateClassData(clf, fieldInfos);
		return generateClass(data, clf.getClassName());
	}
	
	public Bean generate2(ClassInfo clf, FieldInfo[] finfos){
		byte[] data = null;
		try{
			data = clsgen.javassist.ClassGenerateUtil.generate(clf, finfos);			
		}catch(Exception e){
			e.printStackTrace();
		}
		return (Bean)generate(data, clf.getClassName());
	}
	
	private Object generate(byte[] data, String className){
		Object rtnObj = null;
		Class cls = generateClass(data, className);
		try{
			rtnObj = cls.newInstance();
		} catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        } catch (InstantiationException e) {
            throw new RuntimeException(e.getMessage());
        }
        return rtnObj;
	}
	
	private Class generateClass(byte[] data, String className){
		return super.defineClass(className, data, 0, data.length);
	}

}

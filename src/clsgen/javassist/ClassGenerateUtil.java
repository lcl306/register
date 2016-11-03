package clsgen.javassist;

import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import clsgen.info.ClassInfo;
import clsgen.info.FieldInfo;
import clsgen.info.MethodInfo;

//javassist.jar
public class ClassGenerateUtil {
	
	/** Parameter types for call with no parameters. */
	private static final CtClass[] NO_ARGS = {};

	/** Parameter types for call with single int value. */
	//private static final CtClass[] INT_ARGS = { CtClass.intType };
	
	
	static String getMethodName(String fname){
		return "get" +fname.substring(0,1).toUpperCase()+fname.substring(1);
	}
	
	static String setMethodName(String fname){
		return "set" +fname.substring(0,1).toUpperCase()+fname.substring(1);
	}
	
	private static CtClass getType(ClassPool pool, String typeName)
			throws NotFoundException {
		if (typeName == null)
			return CtClass.voidType;
		else
			return pool.get(typeName);
	}

	private static CtClass[] getTypes(ClassPool pool, String[] typeNames)
			throws NotFoundException {
		if (typeNames == null)
			return NO_ARGS;
		CtClass[] types = new CtClass[typeNames.length];
		for (int i = 0; i < typeNames.length; i++) {
			types[i] = getType(pool, typeNames[i]);
		}
		return types;
	}
	
	private static void addField(ClassPool pool, CtClass clas, FieldInfo finfo)
		throws NotFoundException, CannotCompileException{
		CtClass type = pool.get(finfo.getType());
		CtField field = new CtField(type, finfo.getName(), clas);
		clas.addField(field);
	}
	
	private static void addConstructor(CtClass clas)throws CannotCompileException{
		CtConstructor cons = new CtConstructor(NO_ARGS, clas);
		cons.setBody(";");
		clas.addConstructor(cons);
	}
	
	private static CtMethod addMethod(ClassPool pool, CtClass clas, MethodInfo minfo)
		throws NotFoundException, CannotCompileException{
		CtClass rtnType = getType(pool, minfo.getReturn());
		CtMethod meth = new CtMethod(rtnType, minfo.getName(),
				getTypes(pool, minfo.getArgsType()), clas);
		meth.setBody(minfo.getExpress());
		clas.addMethod(meth);
		return meth;
	}
	
	public static byte[] generate(ClassInfo clf, FieldInfo[] finfos)
		throws NotFoundException, CannotCompileException, IOException{
		ClassPool pool = ClassPool.getDefault();
		CtClass clas = pool.makeClass(clf.getClassName());
		if(clf.getInterfaces()!=null){
			for(int i=0; i<clf.getInterfaces().length; i++)
				clas.addInterface(pool.get(clf.getInterfaces()[i]));
		}
		addConstructor(clas);
		
		for(int i=0; i<finfos.length; i++){
			final FieldInfo finfo = finfos[i];
			addField(pool, clas, finfo);
			addMethod(pool, clas, new MethodInfo(){
				public String getName() {
					return setMethodName(finfo.getName());
				}
				public String getReturn() {
					return null;
				}
				public String[] getArgsType() {
					return new String[]{finfo.getType()};
				}
				public String getExpress() {
					//return finfo.getName() +" = (" + finfo.getType() + ")$1;";
					return finfo.getName() +"." + getName() + "($1);";
				}
			});
			addMethod(pool, clas, new MethodInfo(){
				public String getName() {
					return getMethodName(finfo.getName());
				}
				public String getReturn() {
					return finfo.getType();
				}
				public String[] getArgsType() {
					return null;
				}
				public String getExpress() {
					return "return " +finfo.getName() +"." + getName() + "();";
				}
			});
		}
		return clas.toBytecode();
	}
	
	

//	protected byte[] createAccess(Class tclas, Method gmeth,
//	Method smeth, String cname) throws Exception {
//
////	 build generator for the new class
//	String tname = tclas.getName();
//	ClassPool pool = ClassPool.getDefault();
//	CtClass clas = pool.makeClass(cname);
//	clas.addInterface(pool.get("IAccess"));
//	CtClass target = pool.get(tname);
//
////	 add target object field to class
//	CtField field = new CtField(target, "m_target", clas);
//	clas.addField(field);
//
////	 add public default constructor method to class
//	CtConstructor cons = new CtConstructor(NO_ARGS, clas);
//	cons.setBody(";");
//	clas.addConstructor(cons);
//
////	 add public setTarget method
//	CtMethod meth = new CtMethod(CtClass.voidType, "setTarget",
//	new CtClass[] { pool.get("java.lang.Object") }, clas);
//	meth.setBody("m_target = (" + tclas.getName() + ")$1;");
//	clas.addMethod(meth);
//
////	 add public getValue method
//	meth = new CtMethod(CtClass.intType, "getValue", NO_ARGS, clas);
//	meth.setBody("return m_target." + gmeth.getName() + "();");
//	clas.addMethod(meth);
//
////	 add public setValue method
//	meth = new CtMethod(CtClass.voidType, "setValue", INT_ARGS, clas);
//	meth.setBody("m_target." + smeth.getName() + "($1);");
//	clas.addMethod(meth);
//
////	 return binary representation of completed class
//	return clas.toBytecode();
//	}


}

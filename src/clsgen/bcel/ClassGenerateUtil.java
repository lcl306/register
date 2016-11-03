package clsgen.bcel;

import clsgen.info.ClassInfo;
import clsgen.info.FieldInfo;
import clsgen.info.MethodInfo;
import de.fub.bytecode.Constants;
import de.fub.bytecode.classfile.Utility;
import de.fub.bytecode.generic.ClassGen;
import de.fub.bytecode.generic.FieldGen;
import de.fub.bytecode.generic.InstructionFactory;
import de.fub.bytecode.generic.InstructionList;
import de.fub.bytecode.generic.MethodGen;
import de.fub.bytecode.generic.ObjectType;
import de.fub.bytecode.generic.Type;

//bcel.jar
public class ClassGenerateUtil{
	
	private static Type[] getTypes(String[] typeNames){
		if(typeNames==null){
			return Type.NO_ARGS;
		}
		Type[] types = new Type[typeNames.length];
		for(int i=0; i<typeNames.length; i++){
			//Type type = Type.getType(typeNames[i]);
			Type type = new ObjectType(typeNames[i]);
			types[i] = type;
		}
		return types;
	}
	
	private static String[] getArgs(String[] typeNames){
		if(typeNames==null){
			return null;
		}
		String[] args = new String[typeNames.length];
		for(int i=0; i<typeNames.length; i++){
			args[i] = "arg" +i;
		}
		return args;
	}
	
	private static Type getReturn(String rtnName){
		if(rtnName==null){
			return Type.VOID;
		}else{
			return new ObjectType(rtnName);
		}
	}
	
	private static int addField(ClassGen cgen, FieldInfo fieldInfo){
		FieldGen fgen = new FieldGen(Constants.ACC_PRIVATE,	new ObjectType(fieldInfo.getType()), 
				fieldInfo.getName(), cgen.getConstantPool());
		cgen.addField(fgen.getField());
		int findex = cgen.getConstantPool().addFieldref(cgen.getClassName(), fieldInfo.getName(),
				Utility.getSignature(fieldInfo.getType()));
		return findex;
	}
	
	private static void addMethod(ClassGen cgen, InstructionList ilist, MethodInfo minfo){
		MethodGen mgen = new MethodGen(Constants.ACC_PUBLIC, getReturn(minfo.getReturn()),
				getTypes(minfo.getArgsType()), getArgs(minfo.getArgsType()), minfo.getName(), 
				cgen.getClassName(), ilist, cgen.getConstantPool());
		mgen.setMaxStack();
		mgen.setMaxLocals();
		cgen.addMethod(mgen.getMethod());
		ilist.dispose();
	}
	
	private static void addConstruction(ClassGen cgen, InstructionList ilist){
		MethodGen mgen = new MethodGen(Constants.ACC_PUBLIC, Type.VOID,
				Type.NO_ARGS, null, "<init>", cgen.getClassName(), ilist, 
				cgen.getConstantPool());
		mgen.setMaxStack();
		mgen.setMaxLocals();
		cgen.addMethod(mgen.getMethod());
		ilist.dispose();
	}
	
	public static byte[] genrateClassData(ClassInfo clf, FieldInfo[] fieldInfos){
		ClassGen classgen = new ClassGen(clf.getClassName(), clf.getExtendsClass(), 
				clf.getClassName()+".java",Constants.ACC_PUBLIC,
	            clf.getInterfaces());
		//classgen.addEmptyConstructor(Constants.ACC_PUBLIC);
		InstructionFactory ifact = new InstructionFactory(classgen);
		InstructionList ilist = ExpressUtil.doEmptyConstuctor(ifact);
		addConstruction(classgen, ilist);

		for(int i=0; i<fieldInfos.length; i++){
			final FieldInfo finfo = fieldInfos[i];
			int findex = addField(classgen, finfo);
			ilist = ExpressUtil.doGet(findex, finfo, ifact);
			addMethod(classgen, ilist, new MethodInfo(){
				public String getName() {
					return ExpressUtil.getName(finfo.getName());
				}
				public String getReturn() {
					return finfo.getType();
				}
				public String[] getArgsType() {
					return null;
				}
				public String getExpress() {
					return null;
				}
			});
			ilist = ExpressUtil.doSet(findex, finfo, ifact);
			addMethod(classgen, ilist, new MethodInfo(){
				public String getName() {
					return ExpressUtil.setName(finfo.getName());
				}
				public String getReturn() {
					return null;
				}
				public String[] getArgsType() {
					return new String[]{finfo.getType()};
				}
				public String getExpress() {
					return null;
				}
			});
		}
		return classgen.getJavaClass().getBytes();
	}
	
//	public Object generate(ClassInfo clf, MethodInfo[] methodInfos){
//		Object rtnObj = null;
//		ClassGen classgen = new ClassGen(clf.getClassName(), clf.getExtendsClass(), "",
//            Constants.ACC_PUBLIC | Constants.ACC_SUPER,
//            clf.getInterfaces());
//		classgen.addEmptyConstructor(Constants.ACC_PUBLIC);
//		
////		InstructionList ilist = new InstructionList();
////		ilist.append(InstructionConstants.ALOAD_0);
////		ilist.append(ifact.createInvoke("java.lang.Object", "<init>",
////		Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
////		ilist.append(InstructionFactory.createReturn(Type.VOID));
//		
////		MethodGen mgen = new MethodGen(Constants.ACC_PUBLIC, Type.VOID,
////				Type.NO_ARGS, null, "<init>", cname, ilist, pgen);
////				addMethod(mgen, cgen);
//		
//		ConstantPoolGen cp = classgen.getConstantPool();
//		InstructionList il = new InstructionList();
//		
//		for(int i=0; i<methodInfos.length; i++){
//			MethodGen mg = getMethodgen(clf.getClassName(), methodInfos[i], cp, il);
//			classgen.addMethod(mg.getMethod());
//		}
//		byte[] data = classgen.getJavaClass().getBytes();
//		Class cls = super.defineClass(clf.getClassName(),data, 0, data.length);
//		try{
//			rtnObj = cls.newInstance();
//		} catch (IllegalAccessException e) {
//            throw new RuntimeException(e.getMessage());
//        } catch (InstantiationException e) {
//            throw new RuntimeException(e.getMessage());
//        }
//
//		return rtnObj;
//	}
//	
//	private static MethodGen getMethodgen(String className, MethodInfo minfo,
//			ConstantPoolGen cp, InstructionList il){
//		String name = minfo.getName();
//		String rtn = minfo.getReturn();
//		String[] arginfo = minfo.getArgsType();
//		Type[] types = getTypes(arginfo);
//		String[] args = getArgs(arginfo);
//		MethodGen methodgen = new MethodGen(Constants.ACC_PUBLIC, Type.getType(rtn),
//				types, args, name, className, il, cp);
//		methodgen.setMaxStack();
//		methodgen.setMaxLocals();
//		return methodgen;
//	}

}

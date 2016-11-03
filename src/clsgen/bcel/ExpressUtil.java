package clsgen.bcel;

import clsgen.info.FieldInfo;
import de.fub.bytecode.Constants;
import de.fub.bytecode.generic.GETFIELD;
import de.fub.bytecode.generic.InstructionConstants;
import de.fub.bytecode.generic.InstructionFactory;
import de.fub.bytecode.generic.InstructionList;
import de.fub.bytecode.generic.ObjectType;
import de.fub.bytecode.generic.Type;

public class ExpressUtil {
	
	static String getName(String fname){
		return "get" +fname.substring(0,1).toUpperCase()+fname.substring(1);
	}
	
	static String setName(String fname){
		return "set" +fname.substring(0,1).toUpperCase()+fname.substring(1);
	}
	
	static InstructionList doGet(int findex, FieldInfo finfo, InstructionFactory ifact){
		InstructionList ilist = new InstructionList();
		ilist.append(InstructionConstants.ALOAD_0);
		ilist.append(new GETFIELD(findex));
		String methodName = getName(finfo.getName());
		ilist.append(ifact.createInvoke(finfo.getType(), methodName,
		new ObjectType(finfo.getType()), Type.NO_ARGS, Constants.INVOKEVIRTUAL));
		ilist.append(InstructionConstants.IRETURN);
		return ilist;
	}
	
	static InstructionList doSet(int findex, FieldInfo finfo, InstructionFactory ifact){
		InstructionList ilist = new InstructionList();
		ilist.append(InstructionConstants.ALOAD_0);
		ilist.append(new GETFIELD(findex));
		ilist.append(InstructionConstants.ILOAD_1);
		String methodName = setName(finfo.getName());
		ilist.append(ifact.createInvoke(finfo.getType(), methodName,
				Type.VOID, new Type[]{new ObjectType(finfo.getType())}, Constants.INVOKEVIRTUAL));
		ilist.append(InstructionConstants.RETURN);
		return ilist;
	}
	
	static InstructionList doEmptyConstuctor(InstructionFactory ifact){
		InstructionList ilist = new InstructionList();
		ilist.append(InstructionConstants.ALOAD_0);
		ilist.append(ifact.createInvoke("java.lang.Object", "<init>",
		Type.VOID, Type.NO_ARGS, Constants.INVOKESPECIAL));
		ilist.append(InstructionFactory.createReturn(Type.VOID));
		return ilist;
	}
	
//	private void push(InstructionList il, ConstantPoolGen cp, String value){
//		il.append(InstructionConstants.ALOAD_1);
//		il.append(new PUSH(cp, value));
//		il.append(InstructionConstants.IALOAD);
//		il.append(InstructionConstants.IRETURN);
//	}
//	
//	private void push(InstructionList il, ConstantPoolGen cp, Number value){
//		il.append(InstructionConstants.ALOAD_1);
//		il.append(new PUSH(cp, value));
//		il.append(InstructionConstants.IALOAD);
//		il.append(InstructionConstants.IRETURN);
//	}

}

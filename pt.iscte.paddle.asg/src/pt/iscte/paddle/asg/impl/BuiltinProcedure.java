package pt.iscte.paddle.asg.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;

import pt.iscte.paddle.asg.IDataType;
import pt.iscte.paddle.asg.IValueType;
import pt.iscte.paddle.machine.IValue;
import pt.iscte.paddle.machine.impl.Value;

// TODO different types
public class BuiltinProcedure extends Procedure {

	private Method method;
	
	public BuiltinProcedure(Method method) {
		super(method.getName(), IDataType.INT);
		assert isValidForBuiltin(method);
		this.method = method;
		for (Parameter p : method.getParameters()) {
			addParameter(p.getName(), IDataType.INT);
		}
	}

	public static boolean isValidForBuiltin(Method method) {
		if(!Modifier.isPublic(method.getModifiers()) || 
			!Modifier.isStatic(method.getModifiers()) || 
			!validType(method.getReturnType()))
			return false;
		
		for (Class<?> ptype : method.getParameterTypes()) {
			if(!validType(ptype))
				return false;
		}
		return true;
	}
	
	private static boolean validType(Class<?> c) {
		for(IValueType t : IDataType.VALUE_TYPES)
			if(t.matchesPrimitiveType(c))
				return true;
		return false;
	}
	
	private static Object match(Object o, IDataType t) {
		if(t == IDataType.INT)
			return ((Number) o).intValue();
		if(t == IDataType.DOUBLE)
			return ((Number) o).doubleValue();
		if(t == IDataType.BOOLEAN)
			return ((Boolean) o).booleanValue();
		
		return null;
	}
	
	public IValue hookAction(List<IValue> arguments) {
		Object[] args = new Object[arguments.size()];
		for(int i = 0; i < arguments.size(); i++) {
			args[i] = match(arguments.get(i).getValue(), arguments.get(i).getType());
		}
		try {
			Object ret = method.invoke(null, args);
			return Value.create(getReturnType(), ret);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}

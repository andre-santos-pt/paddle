package pt.iscte.paddle.model.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;

import pt.iscte.paddle.interpreter.ICallStack;
import pt.iscte.paddle.interpreter.IValue;
import pt.iscte.paddle.model.IConstant;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.IModule;
import pt.iscte.paddle.model.IType;

class ConstantDeclaration extends Expression implements IConstant {
	private final Module program;
	private final IType type;
	private ILiteral value;
	
	public ConstantDeclaration(Module program, IType type, ILiteral value) {
		this.program = program;
		this.type = type;
		this.value = value;
	}

	@Override
	public IModule getProgram() {
		return program;
	}

	@Override
	public IType getType() {
		return type;
	}

	@Override
	public ILiteral getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return getId();
	}

	@Override
	public List<IExpression> getParts() {
		return ImmutableList.of();
	}

	@Override
	public IValue evalutate(List<IValue> values, ICallStack stack) {
		return stack.getProgramState().getValue(value.getStringValue());
	}

	@Override
	public void setValue(ILiteral value) {
		assert value != null;
		this.value = value;
		setProperty("VALUE", value);
		
		program.executeCommand(new PropertyModifyCommand<IConstant>(this, "VALUE", value));
	}
	
}

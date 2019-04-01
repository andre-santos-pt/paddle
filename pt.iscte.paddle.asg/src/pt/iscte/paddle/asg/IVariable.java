package pt.iscte.paddle.asg;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

public interface IVariable extends ISimpleExpression, IStatement {
	IProgramElement getParent();
	IDataType getType();

	default boolean isRecordField() {
		return getParent() instanceof IRecordType;
	}

	default boolean isLocalVariable() {
		return getParent() instanceof IBlock;
	}

	default IProcedure getProcedure() {
		IProgramElement e = getParent();
		while(e != null && !(e instanceof IProcedure))
			e = ((IBlock) e).getParent();
		
		return e == null ? null : (IProcedure) e;
	}
	
	@Override
	default List<IExpression> getExpressionParts() {
		return ImmutableList.of();
	}
	
	IVariableAddress address();

	IVariableReferenceValue valueOf();

//	default IVariable resolve() {
//		return this;
//	}

	IArrayLengthExpression arrayLength(List<IExpression> indexes);
	default IArrayLengthExpression arrayLength(IExpression ... indexes) {
		return arrayLength(Arrays.asList(indexes));
	}

	IArrayElementExpression arrayElement(List<IExpression> indexes);
	default IArrayElementExpression arrayElement(IExpression ... indexes) {
		return arrayElement(Arrays.asList(indexes));
	}

	IRecordFieldExpression field(IVariable field);
	
	default String getDeclaration() {
		return getType() + " " + getId();
	}
}
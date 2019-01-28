package pt.iscte.paddle.asg;

import java.util.List;

import pt.iscte.paddle.asg.IOperator.OperationType;

/**
 * Immutable
 *
 */
public interface IExpression extends IProgramElement {
	IDataType getType();

	// TODO concretize expression
	//String concretize();
	//	ISourceElement getParent();


	boolean isDecomposable();

	default int getNumberOfParts() {
		return decompose().size();
	}

	List<IExpression> decompose();

	default OperationType getOperationType() {
		return OperationType.OTHER;
	}

	default void accept(IVisitor visitor) {
		visitPart(visitor, this);
	}

	static void visitPart(IVisitor visitor, IExpression part) {
		if(part instanceof IArrayAllocation) {
			IArrayAllocation alloc = (IArrayAllocation) part; 
			if(visitor.visit(alloc))
				part.decompose().forEach(p -> visitPart(visitor, p));
		}
		else if(part instanceof IArrayLengthExpression) {
			IArrayLengthExpression len = (IArrayLengthExpression) part; 
			if(visitor.visit(len))
				part.decompose().forEach(p -> visitPart(visitor, p));
		}
		else if(part instanceof IArrayElementExpression) {
			IArrayElementExpression el = (IArrayElementExpression) part; 
			if(visitor.visit(el))
				part.decompose().forEach(p -> visitPart(visitor, p));
		}
		
		else if(part instanceof IUnaryExpression) {
			IUnaryExpression un = (IUnaryExpression) part; 
			if(visitor.visit(un))
				part.decompose().forEach(p -> visitPart(visitor, p));
		}
		else if(part instanceof IBinaryExpression) {
			IBinaryExpression bi = (IBinaryExpression) part; 
			if(visitor.visit(bi))
				part.decompose().forEach(p -> visitPart(visitor, p));
		}
		
		else if(part instanceof IProcedureCallExpression) {
			IProcedureCallExpression call = (IProcedureCallExpression) part; 
			if(visitor.visit(call))
				part.decompose().forEach(p -> visitPart(visitor, p));
		}

		else if(part instanceof IConstantExpression) {
			IConstantExpression con = (IConstantExpression) part; 
			visitor.visit(con);
		}
		else if(part instanceof ILiteral) {
			ILiteral lit = (ILiteral) part; 
			visitor.visit(lit);
		}
		
		
		else if(part instanceof IStructAllocation) {
			IStructAllocation str = (IStructAllocation) part; 
			visitor.visit(str);
		}
		else if(part instanceof IStructMemberExpression) {
			IStructMemberExpression sm = (IStructMemberExpression) part; 
			visitor.visit(sm);
		}
		
		else if(part instanceof IVariableExpression) {
			IVariableExpression var = (IVariableExpression) part; 
			visitor.visit(var);
		}
		else if(part instanceof IVariableAddress) {
			IVariableAddress varadd = (IVariableAddress) part; 
			visitor.visit(varadd);
		}
		else if(part instanceof IVariableReferenceValue) {
			IVariableReferenceValue varadd = (IVariableReferenceValue) part; 
			visitor.visit(varadd);
		}
		
		else
			assert false: "missing case " + part.getClass().getName();
	}

	interface IVisitor {
		default boolean visit(IArrayAllocation exp) 		{ return true; }
		default boolean visit(IArrayLengthExpression exp) 	{ return true; }
		default boolean visit(IArrayElementExpression exp) 	{ return true; }
		
		default boolean visit(IUnaryExpression exp) 		{ return true; }
		default boolean visit(IBinaryExpression exp) 		{ return true; }
		
		default boolean visit(IProcedureCallExpression exp) { return true; }

		default void 	visit(IConstantExpression exp) 		{ }
		default void 	visit(ILiteral exp) 				{ }
		
		default void 	visit(IStructAllocation exp) 		{ }
		default void 	visit(IStructMemberExpression exp) 	{ }
	
		default void 	visit(IVariableExpression exp) 		{ }
		default void	visit(IVariableAddress exp) 		{ }
		default void	visit(IVariableReferenceValue exp) 	{ }
	}
	
	
}

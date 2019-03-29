package pt.iscte.paddle.asg;

import java.util.List;

/**
 * Mutable
 */
public interface IBlock extends IBlockChild, IStatementContainer {
	IProcedure getProcedure();

	List<IBlockChild> getChildren();

	boolean isEmpty();

	@Override
	default IBlock getBlock() {
		return this;
	}
	
	default void accept(IVisitor visitor) {
		for(IProgramElement s : getChildren()) {
			if(s instanceof IReturn) {
				IReturn ret = (IReturn) s;
				if(visitor.visit(ret) && !ret.getReturnValueType().isVoid())
					ret.getExpression().accept(visitor);
			}
			else if(s instanceof IVariable) {
				IVariable var = (IVariable) s;
				visitor.visit(var);
			}
			else if(s instanceof IArrayElementAssignment) {
				IArrayElementAssignment ass = (IArrayElementAssignment) s;
				if(visitor.visit(ass))
					ass.getExpression().accept(visitor);
			}
			else if(s instanceof IRecordFieldAssignment) {
				IRecordFieldAssignment ass = (IRecordFieldAssignment) s;
				if(visitor.visit(ass))
					ass.getExpression().accept(visitor);
			}
			else if(s instanceof IVariableAssignment) {
				IVariableAssignment ass = (IVariableAssignment) s;
				if(visitor.visit(ass))
					ass.getExpression().accept(visitor);
			}
			
			else if(s instanceof IProcedureCall) {
				IProcedureCall call = (IProcedureCall) s;
				if(visitor.visit(call))
					call.getArguments().forEach(a -> a.accept(visitor));
			}
			else if(s instanceof IBreak) {
				visitor.visit((IBreak) s);
			}
			else if(s instanceof IContinue) {
				visitor.visit((IContinue) s);				
			}
			else if(s instanceof ISelection) {
				ISelection sel = (ISelection) s;
				if(visitor.visit(sel)) {
					sel.getGuard().accept(visitor);
					sel.getBlock().accept(visitor);
					if(sel.hasAlternativeBlock())
						sel.getAlternativeBlock().accept(visitor);
				}
				visitor.endVisit(sel);
			}
			else if(s instanceof ILoop) {
				ILoop loop = (ILoop) s;
				if(visitor.visit(loop)) {
					loop.getGuard().accept(visitor);
					loop.getBlock().accept(visitor);
				}
				visitor.endVisit(loop);
			}
			else if(s instanceof IBlock) { // only single blocks
				IBlock b = (IBlock) s;
				if(visitor.visit(b))
					b.accept(visitor);
				visitor.endVisit(b);
			}
			else
				assert false: "missing case " + s.getClass().getName();
		}
	}

	interface IVisitor extends IExpression.IVisitor {

		// IStatement
		default boolean visit(IReturn returnStatement) 				{ return true; }
		default boolean visit(IArrayElementAssignment assignment) 	{ return true; }
		default boolean visit(IVariableAssignment assignment) 		{ return true; }
		default boolean visit(IRecordFieldAssignment assignment) 	{ return true; }
		default boolean visit(IProcedureCall call) 					{ return true; }

		// IControlStructure
		default boolean visit(ISelection selection) 				{ return true; }
		default void endVisit(ISelection selection) 				{ }

		default boolean visit(ILoop loop) 							{ return true; }
		default void endVisit(ILoop loop) 							{ }

		// other
		default boolean visit(IBlock block) 						{ return true; }
		default void endVisit(IBlock block) 						{ }

		default void 	visit(IBreak breakStatement) 				{ }
		default void 	visit(IContinue continueStatement) 			{ }

		// TODO missing because it is not statement, only appears on expressions
		default void	visit(IVariable variable)					{ }
	}

}

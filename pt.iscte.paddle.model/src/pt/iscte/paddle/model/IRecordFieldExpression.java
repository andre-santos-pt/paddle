package pt.iscte.paddle.model;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.interpreter.ICallStack;
import pt.iscte.paddle.interpreter.IRecord;
import pt.iscte.paddle.interpreter.IReference;
import pt.iscte.paddle.interpreter.NullPointerError;

public interface IRecordFieldExpression extends ISimpleExpression {

	IExpression getTarget();
	IVariableDeclaration getField();
	
	IRecordFieldExpression field(IVariableDeclaration field);
	
	IArrayElement element(List<IExpression> indexes);
	default IArrayElement element(IExpression ... indexes) {
		return element(Arrays.asList(indexes));
	}

	default IRecord resolveTarget(ICallStack stack) throws ExecutionError {
		IExpression f = getTarget();
		Queue<IVariableDeclaration> queue = new ArrayDeque<>();
		
		while(f instanceof IRecordFieldExpression) {
			queue.add(((IRecordFieldExpression) f).getField());
			f = ((IRecordFieldExpression) f).getTarget();
		}
		IReference ref = stack.getTopFrame().getVariableStore((IVariableDeclaration) f);
		if(ref.isNull())
			throw new NullPointerError(f);
			
		IRecord r = (IRecord) ref.getTarget();
		while(!queue.isEmpty()) {
			ref = r.getField(queue.remove());
			r = (IRecord) ref.getTarget();
		}
		
		return r;
	}
}
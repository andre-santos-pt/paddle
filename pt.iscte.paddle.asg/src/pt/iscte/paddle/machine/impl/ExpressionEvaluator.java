package pt.iscte.paddle.machine.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import pt.iscte.paddle.asg.IExpression;
import pt.iscte.paddle.asg.IProcedureCall;
import pt.iscte.paddle.machine.ExecutionError;
import pt.iscte.paddle.machine.ICallStack;
import pt.iscte.paddle.machine.IExpressionEvaluator;
import pt.iscte.paddle.machine.IValue;

public class ExpressionEvaluator implements IExpressionEvaluator {

	private ICallStack callStack;
	private Stack<IExpression> expStack;
	private Stack<IValue> valueStack;

	public ExpressionEvaluator(IExpression expression, ICallStack callStack)  {
		this.callStack = callStack;
		expStack = new Stack<IExpression>();
		valueStack = new Stack<IValue>();
		expStack.push(expression);
	}


	@Override
	public boolean isComplete() {
		return expStack.isEmpty();
	}

	@Override
	public IValue evaluate() throws ExecutionError {
		while(!expStack.isEmpty())
			step();

		return getValue();
	}

	@Override
	public IValue getValue() {
		assert isComplete();
		return valueStack.peek();
	}

	@Override
	public Step step() throws ExecutionError {
		assert !isComplete();

		while(expStack.peek().isDecomposable() && valueStack.size() < expStack.peek().getNumberOfParts()) {
			expStack.peek().decompose().forEach(e -> expStack.push(e));

			while(!expStack.peek().isDecomposable())
				valueStack.push(callStack.getTopFrame().evaluate(expStack.pop(), ImmutableList.of()));
		}

		int parts = expStack.peek().getNumberOfParts();
		List<IValue> values = new ArrayList<>();
		while(parts-- > 0)
			values.add(valueStack.pop());

		IValue val = callStack.getTopFrame().evaluate(expStack.peek(), values);
		if(val == null) {
			IProcedureCall callExp = (IProcedureCall) expStack.pop();
			expStack.push(new ProcedureReturnExpression(callExp.getProcedure()));
		}
		else
			valueStack.push(val);

		return new Step(val == null ? null : expStack.pop(), val);
	}
}
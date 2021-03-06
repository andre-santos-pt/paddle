package pt.iscte.paddle.model.impl;

import java.util.List;

import com.google.common.collect.ImmutableList;

import pt.iscte.paddle.interpreter.ExecutionError;
import pt.iscte.paddle.interpreter.ICallStack;
import pt.iscte.paddle.interpreter.IEvaluable;
import pt.iscte.paddle.interpreter.IValue;
import pt.iscte.paddle.model.IArrayElement;
import pt.iscte.paddle.model.IArrayLength;
import pt.iscte.paddle.model.IConditionalExpression;
import pt.iscte.paddle.model.IExpression;
import pt.iscte.paddle.model.IExpressionIterator;
import pt.iscte.paddle.model.IRecordFieldExpression;
import pt.iscte.paddle.model.IVariableDeclaration;

abstract class Expression extends ProgramElement implements IEvaluable, IExpression {

	Expression(String...flags) {
		super(flags);
	}
	
	@Override
	public IRecordFieldExpression field(IVariableDeclaration field) {
		return new RecordFieldExpression(this, field);
	}	
	
	@Override
	public IArrayLength length(List<IExpression> indexes) {
		return new ArrayLength(this, indexes);
	}
	
	@Override
	public IArrayElement element(List<IExpression> indexes) {
		return new ArrayElement(this, indexes);
	}
	
	@Override
	public IConditionalExpression conditional(IExpression trueCase, IExpression falseCase) {
		return new Conditional(this, trueCase, falseCase);
	}	

	// TODO evaluate only one
	// ideia: decompose() -> iterador que para quando nao e preciso mais
	static class Conditional extends Expression implements IConditionalExpression, IEvaluable {
		private ImmutableList<IExpression> parts;
		
		public Conditional(IExpression condition, IExpression trueCase, IExpression falseCase) {
			parts = ImmutableList.of(condition, trueCase, falseCase);
		}
		
		@Override
		public List<IExpression> getParts() {
			return parts;
		}

		@Override
		public IConditionalExpression conditional(IExpression trueCase, IExpression falseCase) {
			return new Conditional(this, trueCase, falseCase);
		}

		@Override
		public IExpression getConditional() {
			return parts.get(0);
		}

		@Override
		public IExpression getTrueExpression() {
			return parts.get(1);
		}

		@Override
		public IExpression getFalseExpression() {
			return parts.get(2);
		}	
		
		@Override
		public IValue evalutate(List<IValue> values, ICallStack stack) throws ExecutionError {
			return values.get(0).isTrue() ? values.get(1) : values.get(2);
		}

		
		class Iterator implements IExpressionIterator {
			boolean condition = false;
			boolean over = false;

			@Override
			public IExpression next(IValue lastEvaluation) {
				if(!condition) {
					assert lastEvaluation == null;
					condition = true;
					return getConditional();
				}
				else {
					assert lastEvaluation != null;
					over = true;
					if(lastEvaluation.isTrue())
						return getTrueExpression();
					else
						return getFalseExpression();
				}
			}

			@Override
			public boolean hasNext(IValue lastEvaluation) {
				return !over;
			}
		}
	}
}

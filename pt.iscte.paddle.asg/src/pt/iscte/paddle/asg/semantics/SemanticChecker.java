package pt.iscte.paddle.asg.semantics;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import pt.iscte.paddle.asg.IArrayAllocation;
import pt.iscte.paddle.asg.IArrayElementAssignment;
import pt.iscte.paddle.asg.IArrayElementExpression;
import pt.iscte.paddle.asg.IArrayLengthExpression;
import pt.iscte.paddle.asg.IBinaryExpression;
import pt.iscte.paddle.asg.IBlock;
import pt.iscte.paddle.asg.IBreak;
import pt.iscte.paddle.asg.IConstant;
import pt.iscte.paddle.asg.IContinue;
import pt.iscte.paddle.asg.ILiteral;
import pt.iscte.paddle.asg.ILoop;
import pt.iscte.paddle.asg.IModule;
import pt.iscte.paddle.asg.IProcedure;
import pt.iscte.paddle.asg.IProcedureCall;
import pt.iscte.paddle.asg.IProcedureCallExpression;
import pt.iscte.paddle.asg.IReturn;
import pt.iscte.paddle.asg.ISelection;
import pt.iscte.paddle.asg.IStructAllocation;
import pt.iscte.paddle.asg.IStructMemberAssignment;
import pt.iscte.paddle.asg.IStructMemberExpression;
import pt.iscte.paddle.asg.IStructType;
import pt.iscte.paddle.asg.IUnaryExpression;
import pt.iscte.paddle.asg.IVariable;
import pt.iscte.paddle.asg.IVariableAddress;
import pt.iscte.paddle.asg.IVariableAssignment;
import pt.iscte.paddle.asg.IVariableReferenceValue;

public class SemanticChecker {
	private ISemanticChecker checker;
	private List<Rule> rules;

	public SemanticChecker(ISemanticChecker checker) {
		this.checker = checker;
	}

	public List<ISemanticProblem> check(IModule program) {
		rules = new ArrayList<Rule>();
		for (Class<? extends Rule> r : checker.getRules()) {
			try {
				Rule rule = r.newInstance();
				rule.setup(program);
				rules.add(rule);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		CompleteVisitor v = new CompleteVisitor();
		program.accept(v);
		return v.getProblems();
	}

	public class CompleteVisitor implements IModule.IVisitor {
		List<ISemanticProblem> getProblems() {
			List<ISemanticProblem> list = new ArrayList<>();
			rules.forEach(r -> list.addAll(r.getProblems()));
			return list;
		}

		@Override
		public void visit(IConstant constant) {
			rules.forEach(r -> r.visit(constant));
		}
		
		@Override
		public boolean visit(IStructType struct) {
			rules.forEach(r -> r.visit(struct));
			return true;
		}
		
		@Override
		public boolean visit(IProcedure procedure) {
			rules.forEach(r -> r.visit(procedure));
			return true;
		}
		
		@Override
		public boolean visit(IBlock block) {
			rules.forEach(r -> r.visit(block));
			return true;
		}

		@Override
		public void visit(IVariable variable) {
			rules.forEach(r -> r.visit(variable));
		}

		@Override
		public boolean visit(IReturn returnStatement) {
			rules.forEach(r -> r.visit(returnStatement));
			return true;
		}

		@Override
		public boolean visit(IArrayElementAssignment assignment) {
			rules.forEach(r -> r.visit(assignment));
			return true;
		}

		@Override
		public boolean visit(IVariableAssignment assignment) {
			rules.forEach(r -> r.visit(assignment));
			return true;
		}

		@Override
		public boolean visit(IStructMemberAssignment assignment) {
			rules.forEach(r -> r.visit(assignment));
			return true;
		}

		@Override
		public boolean visit(IProcedureCall call) {
			rules.forEach(r -> r.visit(call));
			return true;
		}

		@Override
		public void visit(IContinue continueStatement) {
			rules.forEach(r -> r.visit(continueStatement));
		}

		@Override
		public boolean visit(ISelection selection) {
			rules.forEach(r -> r.visit(selection));
			return true;
		}

		@Override
		public boolean visit(ILoop loop) {
			rules.forEach(r -> r.visit(loop));
			return true;	
		}

		@Override
		public boolean visit(IArrayAllocation exp) {
			rules.forEach(r -> r.visit(exp));
			return true;
		}

		@Override
		public boolean visit(IArrayLengthExpression exp) {
			rules.forEach(r -> r.visit(exp));
			return true;
		}

		@Override
		public boolean visit(IBinaryExpression exp) {
			rules.forEach(r -> r.visit(exp));
			return true;
		}

//		@Override
//		public void visit(IConstantExpression exp) {
//			rules.forEach(r -> r.visit(exp));
//		}

		@Override
		public boolean visit(IProcedureCallExpression exp) {
			rules.forEach(r -> r.visit(exp));
			return true;
		}

		@Override
		public void visit(IStructAllocation exp) {
			rules.forEach(r -> r.visit(exp));
		}

		@Override
		public void visit(IStructMemberExpression exp) {
			rules.forEach(r -> r.visit(exp));
		}

		@Override
		public boolean visit(IUnaryExpression exp) {
			rules.forEach(r -> r.visit(exp));
			return true;
		}

//		@Override
//		public void visit(IVariableExpression exp) {
//			rules.forEach(r -> r.visit(exp));
//		}
//		@Override
//		public void visit(IVariable exp) {
//			rules.forEach(r -> r.visit(exp));
//		}
		
		@Override
		public void visit(IVariableAddress exp) {
			rules.forEach(r -> r.visit(exp));
		}
		
		@Override
		public void visit(IVariableReferenceValue exp) {
			rules.forEach(r -> r.visit(exp));
		}
		
		@Override
		public boolean visit(IArrayElementExpression exp) {
			rules.forEach(r -> r.visit(exp));
			return true;
		}

		@Override
		public void visit(ILiteral exp) {
			rules.forEach(r -> r.visit(exp));
		}
		
		@Override
		public void visit(IBreak breakStatement) {
			rules.forEach(r -> r.visit(breakStatement));
		}
	}
	
	static {
		assert hasAllVisitMethods();
	}
	
	private static boolean hasAllVisitMethods() {
		boolean all = true;
		for (Method method : IModule.IVisitor.class.getMethods()) {
			try {
				if(method.getName().equals("visit"))
					CompleteVisitor.class.getDeclaredMethod(method.getName(), method.getParameterTypes()[0]);
			} catch (NoSuchMethodException e) {
				System.err.println(CompleteVisitor.class.getSimpleName() + " missing: " + method);
				all = false;
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return all;
	}


}

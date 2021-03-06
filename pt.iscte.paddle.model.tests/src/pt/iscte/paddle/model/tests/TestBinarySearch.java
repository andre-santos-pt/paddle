package pt.iscte.paddle.model.tests;

import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.EQUAL;
import static pt.iscte.paddle.model.IOperator.IDIV;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IOperator.SMALLER_EQ;
import static pt.iscte.paddle.model.IOperator.SUB;
import static pt.iscte.paddle.model.IType.BOOLEAN;
import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.interpreter.IExecutionData;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;
import pt.iscte.paddle.model.cfg.IBranchNode;
import pt.iscte.paddle.model.cfg.IControlFlowGraph;
import pt.iscte.paddle.model.cfg.IStatementNode;

public class TestBinarySearch extends BaseTest {

	IProcedure binarySearch = module.addProcedure(BOOLEAN);
	IVariableDeclaration array = binarySearch.addParameter(INT.array().reference());
	IVariableDeclaration e = binarySearch.addParameter(INT);
	IBlock body = binarySearch.getBody();

	IVariableDeclaration l = body.addVariable(INT, INT.literal(0));
	IVariableDeclaration r = body.addVariable(INT, SUB.on(array.length(), INT.literal(1)));

	ILoop loop = body.addLoop(SMALLER_EQ.on(l, r));
	IVariableDeclaration m = loop.addVariable(INT, ADD.on(l, IDIV.on(SUB.on(r, l), INT.literal(2)) ));

	ISelection iffound = loop.addSelection(EQUAL.on(array.element(m), e));
	IReturn retTrue = iffound.getBlock().addReturn(BOOLEAN.literal(true));

	ISelection ifnot = loop.addSelectionWithAlternative(SMALLER.on(array.element(m), e));
	IVariableAssignment lAss = ifnot.getBlock().addAssignment(l, ADD.on(m, INT.literal(1)));
	IVariableAssignment rAss = ifnot.getAlternativeBlock().addAssignment(r, SUB.on(m, INT.literal(1)));

	IReturn ret = body.addReturn(BOOLEAN.literal(false));
	
	protected IProcedure main() {
		IProcedure test = module.addProcedure(BOOLEAN);
		IVariableDeclaration e = test.addParameter(INT);
		IBlock body = test.getBody();
		IVariableDeclaration a = body.addVariable(INT.array().reference(), INT.array().heapAllocation(INT.literal(10)));
		body.addArrayElementAssignment(a, INT.literal(-2), INT.literal(0));
		body.addArrayElementAssignment(a, INT.literal(0), INT.literal(1));
		body.addArrayElementAssignment(a, INT.literal(1), INT.literal(2));
		body.addArrayElementAssignment(a, INT.literal(4), INT.literal(3));
		body.addArrayElementAssignment(a, INT.literal(5), INT.literal(4));
		body.addArrayElementAssignment(a, INT.literal(8), INT.literal(5));
		body.addArrayElementAssignment(a, INT.literal(10), INT.literal(6));
		body.addArrayElementAssignment(a, INT.literal(11), INT.literal(7));
		body.addArrayElementAssignment(a, INT.literal(20), INT.literal(8));
		body.addArrayElementAssignment(a, INT.literal(23), INT.literal(9));
		body.addReturn(binarySearch.expression(a, e));
		return test;
	}
	
	@Case("1")
	public void testTrue(IExecutionData data) {
		isTrue(data.getReturnValue());
//		System.out.println(data.getTotalAssignments()); //TODO assignments
	}
	
	@Case("-4")
	public void testFalse(IExecutionData data) {
		isFalse(data.getReturnValue());
	}
	
	@Override
	protected IControlFlowGraph cfg() {
		IControlFlowGraph cfg = IControlFlowGraph.create(binarySearch);

		IBranchNode b_loop = cfg.newBranch(loop.getGuard());

		IBranchNode b_ifFound = cfg.newBranch(iffound.getGuard());
		b_loop.setBranch(b_ifFound);

		IStatementNode s_retTrue = cfg.newStatement(retTrue);
		b_ifFound.setBranch(s_retTrue);
		s_retTrue.setNext(cfg.getExitNode());

		IBranchNode b_ifNot = cfg.newBranch(ifnot.getGuard());
		b_ifFound.setNext(b_ifNot);

		IStatementNode s_lAss = cfg.newStatement(lAss);
		b_ifNot.setBranch(s_lAss);
		s_lAss.setNext(b_loop);

		IStatementNode s_rAss = cfg.newStatement(rAss);
		b_ifNot.setNext(s_rAss);
		s_rAss.setNext(b_loop);

		IStatementNode s_retFalse = cfg.newStatement(ret);
		s_retFalse.setNext(cfg.getExitNode());
		return cfg;
	}
	
}

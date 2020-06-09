package pt.iscte.paddle.model.tests;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IType.INT;

import org.junit.Test;

import pt.iscte.paddle.interpreter.IArray;
import pt.iscte.paddle.interpreter.IExecutionData;
import pt.iscte.paddle.interpreter.IReference;
import pt.iscte.paddle.model.IArrayElementAssignment;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IReturn;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;

public class TestNaturals extends BaseTest  {
	IProcedure naturals = module.addProcedure(INT.array().reference());
	IVariableDeclaration n = naturals.addParameter(INT);
	IBlock body = naturals.getBody();
	IVariableDeclaration array = body.addVariable(INT.array().reference());
	IVariableAssignment ass1 = body.addAssignment(array, INT.array().heapAllocation(n));
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));
	ILoop loop = body.addLoop(SMALLER.on(i, n));
	IArrayElementAssignment ass2 = loop.addArrayElementAssignment(array, ADD.on(i, INT.literal(1)), i);
	IVariableAssignment ass3 = loop.addAssignment(i, ADD.on(i, INT.literal(1)));
	IReturn addReturn = body.addReturn(array);
	
	@Test
	public void test() {
		assertFalse(naturals.isConstantTime());
	}
	
	@Case("0")
	public void testEmpty(IExecutionData data) {
		IArray array = (IArray) ((IReference) data.getReturnValue()).getTarget();
		assertEquals(0, array.getLength());
	}
	
	@Case("5")
	public void lengthFive(IExecutionData data) {
		IArray array = (IArray) ((IReference) data.getReturnValue()).getTarget();
		assertEquals(5, array.getLength());
		for(int x = 0; x < 5; x++)
			equal(x+1, array.getElement(x));
	}
}

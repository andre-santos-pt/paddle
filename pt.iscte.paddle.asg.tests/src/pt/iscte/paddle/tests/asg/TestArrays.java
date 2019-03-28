package pt.iscte.paddle.tests.asg;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static pt.iscte.paddle.asg.IDataType.INT;
import static pt.iscte.paddle.asg.ILiteral.literal;
import static pt.iscte.paddle.asg.IOperator.ADD;
import static pt.iscte.paddle.asg.IOperator.SMALLER;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import pt.iscte.paddle.asg.IArrayElementAssignment;
import pt.iscte.paddle.asg.IBlock;
import pt.iscte.paddle.asg.ILoop;
import pt.iscte.paddle.asg.IModule;
import pt.iscte.paddle.asg.IProcedure;
import pt.iscte.paddle.asg.IReturn;
import pt.iscte.paddle.asg.IVariable;
import pt.iscte.paddle.asg.IVariableAssignment;
import pt.iscte.paddle.machine.IArray;
import pt.iscte.paddle.machine.IExecutionData;
import pt.iscte.paddle.machine.IValue;
import pt.iscte.paddle.tests.asg.TestArrays.Naturals;

@RunWith(Suite.class)

@Suite.SuiteClasses({
	Naturals.class
})
public class TestArrays {

	public static class Naturals extends BaseTest {
		IProcedure naturals = module.addProcedure(INT.array());
		IVariable n = naturals.addParameter(INT);
		IBlock body = naturals.getBody();
		IVariable v = body.addVariable(INT.array());
		IVariableAssignment addAssignment = body.addAssignment(v, INT.array().allocation(n));
		IVariable i = body.addVariable(INT, literal(0));
		ILoop loop = body.addLoop(SMALLER.on(i, n));
		IArrayElementAssignment addArrayElementAssignment = loop.addArrayElementAssignment(v, ADD.on(i, literal(1)), i);
		IVariableAssignment addAssignment2 = loop.addAssignment(i, ADD.on(i, literal(1)));
		IReturn addReturn = body.addReturn(v);
		
		protected void commonAsserts(IExecutionData data) {
			IValue returnValue = data.getReturnValue();
			assertNotEquals(IValue.NULL, returnValue);
			assertTrue(returnValue instanceof IArray);
		}
		
		@Case("5")
		public void lengthFive(IExecutionData data) {
			IArray array = (IArray) data.getReturnValue();
			assertEquals(5, array.getLength());
			for(int x = 0; x < 5; x++)
				assertEqual(x+1, array.getElement(x));
		}
		
		@Case("0")
		public void zeroLength(IExecutionData data) {
			IArray array = (IArray) data.getReturnValue();
			assertEquals(0, array.getLength());
		}
	}


	public void testContainsReturn() {

	}

	public void testContainsBreak() {

	}

	public void testReplaceContinue() {

	}

	public void testMerge() {

	}
}

package pt.iscte.paddle.model.tests;
import static org.junit.Assert.assertEquals;
import static pt.iscte.paddle.model.IOperator.ADD;
import static pt.iscte.paddle.model.IOperator.SMALLER;
import static pt.iscte.paddle.model.IOperator.SUB;
import static pt.iscte.paddle.model.IType.INT;
import static pt.iscte.paddle.model.IType.VOID;

import java.util.Arrays;

import pt.iscte.paddle.interpreter.IArray;
import pt.iscte.paddle.interpreter.IExecutionData;
import pt.iscte.paddle.model.IBlock;
import pt.iscte.paddle.model.ILiteral;
import pt.iscte.paddle.model.ILoop;
import pt.iscte.paddle.model.IProcedure;
import pt.iscte.paddle.model.IProcedureCall;
import pt.iscte.paddle.model.ISelection;
import pt.iscte.paddle.model.IVariableAssignment;
import pt.iscte.paddle.model.IVariableDeclaration;

public class TestSelectionSort extends BaseTest {

	private IProcedure swap = importProcedure(TestSwap.class, "swap");
	
	IProcedure sort = module.addProcedure(VOID);
	IVariableDeclaration array = sort.addParameter(INT.array().reference());
	IBlock body = sort.getBody();
	IVariableDeclaration i = body.addVariable(INT, INT.literal(0));

	ILoop outloop = body.addLoop(SMALLER.on(i, SUB.on(array.length(), INT.literal(1))));
	IVariableDeclaration min = outloop.addVariable(INT, i);
	IVariableDeclaration j = outloop.addVariable(INT, ADD.on(i, INT.literal(1)));

	ILoop inloop = outloop.addLoop(SMALLER.on(j, array.length()));
	ISelection ifstat = inloop.addSelection(SMALLER.on(array.element(j), array.element(min)));
	IVariableAssignment minAss = ifstat.getBlock().addAssignment(min, j);
	IVariableAssignment jInc = inloop.addIncrement(j);
	IProcedureCall swapCall = outloop.addCall(swap, array, i, min);
	IVariableAssignment iInc = outloop.addIncrement(i);
	
	private IVariableDeclaration a;
	
	private int[] integers = {-2, 10, 1, 0, 5, 8, 120, 211, 20, 13};
	private ILiteral[] literals = literalIntArray(integers);
	
	protected IProcedure main() {
		IProcedure test = module.addProcedure(VOID);
		IBlock body = test.getBody();
		a = body.addVariable(INT.array().reference(), INT.array().heapAllocation(INT.literal(literals.length)));
		
		for(int i = 0; i < literals.length; i++)
			body.addArrayElementAssignment(a, literals[i], INT.literal(i));

		body.addCall(sort, a);
		return test;
	}
	
	

	@Case
	public void test(IExecutionData data) {
		int[] sorted = Arrays.copyOf(integers, integers.length);
		Arrays.sort(sorted);
		IArray array = (IArray) data.getVariableValue(a);
		for(int i = 0; i < integers.length; i++)
			assertEquals(new Integer(sorted[i]), new Integer(array.getElement(i).toString()));
		
	}
}

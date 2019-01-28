package pt.iscte.paddle.tests.asg;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import pt.iscte.paddle.asg.IBinaryExpression;
import pt.iscte.paddle.asg.IDataType;
import pt.iscte.paddle.asg.IFactory;
import pt.iscte.paddle.asg.IModule;
import pt.iscte.paddle.asg.IOperator;
import pt.iscte.paddle.asg.IProcedure;
import pt.iscte.paddle.asg.IUnaryExpression;
import pt.iscte.paddle.asg.IVariable;
import pt.iscte.paddle.machine.IExecutionData;
import pt.iscte.paddle.machine.IMachine;
import pt.iscte.paddle.machine.IProgramState;
import pt.iscte.paddle.machine.IValue;

public class TestBooleanFuncions {
	private static IModule program;
	private static IProcedure evenFunc;
	private static IProcedure oddFunc;
	private static IProcedure oddNotEvenFunc;
	
	private static IProcedure withinIntervalFunc;
	private static IProcedure outsideIntervalFunc;

	
	private static IFactory factory = IFactory.INSTANCE;
	
	@BeforeClass
	public static void setup() {
		program = factory.createModule("BooleanFunctions");
		evenFunc = createIsEven();
		oddFunc = createIsOdd();
		oddNotEvenFunc = createIsOddNotEven();
		withinIntervalFunc = createWithinInterval();
		outsideIntervalFunc = createOutsideInterval();
		System.out.println(program);
	}

	private static void assertTrueValue(IValue value) {
		assertTrue(value.getValue().equals(Boolean.TRUE));
	}
	
	private static void assertFalseValue(IValue value) {
		assertTrue(value.getValue().equals(Boolean.FALSE));
	}

	private static IProcedure createIsEven() {
		IProcedure f = program.addProcedure("isEven", IDataType.BOOLEAN);
		IVariable nParam = f.addParameter("n", IDataType.INT);
		
		IBinaryExpression e = factory.binaryExpression(IOperator.EQUAL,
				factory.binaryExpression(IOperator.MOD, nParam.expression(), factory.literal(2)),
				factory.literal(0));
		
		f.getBody().addReturnStatement(e);
		return f;
	}
	
	@Test
	public void testIsEven() {
		IProgramState state = IMachine.create(program);
		IExecutionData dataTrue = state.execute(evenFunc, "6");
		assertTrueValue(dataTrue.getReturnValue());
		
		IExecutionData dataFalse = state.execute(evenFunc, "7");
		assertFalseValue(dataFalse.getReturnValue());
	}
	

	private static IProcedure createIsOdd() {
		IProcedure f = program.addProcedure("isOdd", IDataType.BOOLEAN);
		IVariable nParam = f.addParameter("n", IDataType.INT);
		
		IBinaryExpression e = factory.binaryExpression(IOperator.DIFFERENT,
				factory.binaryExpression(IOperator.MOD, nParam.expression(), factory.literal(2)),
				factory.literal(0));
		
		f.getBody().addReturnStatement(e);
		return f;
	}
	
	private static IProcedure createIsOddNotEven() {
		IProcedure f = program.addProcedure("isOddNotEven", IDataType.BOOLEAN);
		IVariable nParam = f.addParameter("n", IDataType.INT);
		
		IUnaryExpression e = factory.unaryExpression(IOperator.NOT, evenFunc.callExpression(nParam.expression()));
		f.getBody().addReturnStatement(e);
		return f;
	}
	
	@Test
	public void testIsOdd() {
		IProgramState state = IMachine.create(program);
		IExecutionData dataTrue = state.execute(oddFunc, "7");
		assertTrueValue(dataTrue.getReturnValue());
		
		IExecutionData dataFalse = state.execute(oddFunc, "6");
		assertFalseValue(dataFalse.getReturnValue());
	}
	
	@Test
	public void testIsOddNotEven() {
		IProgramState state = IMachine.create(program);
		IExecutionData dataTrue = state.execute(oddNotEvenFunc, "7");
		assertTrueValue(dataTrue.getReturnValue());
		
		IExecutionData dataFalse = state.execute(oddNotEvenFunc, "6");
		assertFalseValue(dataFalse.getReturnValue());
	}
	
	
	private static IProcedure createWithinInterval() {
		IProcedure f = program.addProcedure("withinInterval", IDataType.BOOLEAN);
		IVariable nParam = f.addParameter("n", IDataType.INT);
		IVariable aParam = f.addParameter("a", IDataType.INT);
		IVariable bParam = f.addParameter("b", IDataType.INT);
		
		IBinaryExpression lower = factory.binaryExpression(IOperator.GREATER_EQ, nParam.expression(), aParam.expression());
		IBinaryExpression upper = factory.binaryExpression(IOperator.SMALLER_EQ, nParam.expression(), bParam.expression());
		IBinaryExpression e = factory.binaryExpression(IOperator.AND, lower, upper);
		f.getBody().addReturnStatement(e);
		return f;
	}
	
	@Test
	public void testWithinInterval() {
		IProgramState state = IMachine.create(program);
		IExecutionData dataTrue = state.execute(withinIntervalFunc, "6", "4", "8");
		assertTrueValue(dataTrue.getReturnValue());
		
		IExecutionData dataFalse = state.execute(withinIntervalFunc, "6", "7", "10");
		assertFalseValue(dataFalse.getReturnValue());
	}
	
	private static IProcedure createOutsideInterval() {
		IProcedure f = program.addProcedure("ousideInterval", IDataType.BOOLEAN);
		IVariable nParam = f.addParameter("n", IDataType.INT);
		IVariable aParam = f.addParameter("a", IDataType.INT);
		IVariable bParam = f.addParameter("b", IDataType.INT);
		
		IBinaryExpression lower = factory.binaryExpression(IOperator.SMALLER, nParam.expression(), aParam.expression());
		IBinaryExpression upper = factory.binaryExpression(IOperator.GREATER, nParam.expression(), bParam.expression());
		IBinaryExpression e = factory.binaryExpression(IOperator.OR, lower, upper);
		f.getBody().addReturnStatement(e);
		return f;
	}
	
	@Test
	public void testOutsideInterval() {
		IProgramState state = IMachine.create(program);
		IExecutionData dataTrue = state.execute(outsideIntervalFunc, "3", "4", "8");
		assertTrueValue(dataTrue.getReturnValue());
		
		IExecutionData dataFalse = state.execute(outsideIntervalFunc, "7", "7", "10");
		assertFalseValue(dataFalse.getReturnValue());
	}
	
}

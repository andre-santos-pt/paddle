import pt.iscte.paddle.asg.IDataType;
import pt.iscte.paddle.asg.IFactory;
import pt.iscte.paddle.asg.IModule;
import pt.iscte.paddle.asg.IOperator;
import pt.iscte.paddle.asg.IProcedure;
import pt.iscte.paddle.asg.ISelection;
import pt.iscte.paddle.asg.IVariable;
import pt.iscte.paddle.machine.ExecutionError;
import pt.iscte.paddle.machine.IMachine;
import pt.iscte.paddle.machine.IProgramState;

public class Test {

	public static void main(String[] args) throws ExecutionError {
		IFactory factory = IFactory.INSTANCE;

		IModule program = factory.createModule("test");

		IProcedure proc = program.addProcedure("inc", IDataType.DOUBLE);
		IVariable nParam = proc.addParameter("n", IDataType.INT);

//		IProcedureCallExpression randomCall = program.getProcedure("random").callExpression();

		proc.getBody().addReturnStatement(factory.literal(3.2));

		//		IVariableDeclaration rVar = proc.variableDeclaration("r", program.getDataType("double"));
		//		IVariableAssignment rAss = rVar.assignment(randomCall);

		//		ILiteral lit = factory.literal(4);
		//		IBinaryExpression e = factory.binaryExpression(IOperator.ADD, rVar.expression(), lit);


		//		IVariableAssignment ass2 = rVar.assignment(e);
		//		proc.returnStatement(rVar.expression());

		IProcedure main = program.addProcedure("main", IDataType.DOUBLE);
//		IVariableDeclaration var2 = main.addVariableDeclaration("b", IDataType.INT);	
//		IVariableAssignment ass3 = var2.addAssignment(proc.callExpression(factory.literal(2)));
//		var2.addAssignment(factory.literal(4))
		IVariable a = main.getBody().addVariable("a", IDataType.INT);
		ISelection iff = main.getBody().addSelection(factory.literal(true));
		ISelection iff2 = iff.addSelection(factory.binaryExpression(IOperator.GREATER, factory.literal(4), factory.literal(2)));
		iff2.addReturnStatement(factory.literal(-1));
		a.addAssignment(factory.literal(2));
		main.getBody().addReturnStatement(a.expression());

		System.out.println(program);
		
		IProgramState state = IMachine.create(program);

		state.execute(main);
		
		//		List<ISemanticProblem> problems = program.validateSematics();
//		if(problems.isEmpty())
//			state.execute(main);
//		else
//			for (ISemanticProblem p : program.validateSematics()) {
//				System.err.println(p);
//			}


	}

}

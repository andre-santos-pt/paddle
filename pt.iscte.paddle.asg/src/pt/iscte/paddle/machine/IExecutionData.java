package pt.iscte.paddle.machine;

import java.util.Map;

import pt.iscte.paddle.asg.IOperator.OperationType;
import pt.iscte.paddle.asg.IProcedure;
import pt.iscte.paddle.asg.IVariable;

public interface IExecutionData {
//	Map<IProcedure, Integer> getNumberOfProcedureCalls();
	Map<IProcedure, Integer> getAssignmentData();
//	Map<IProcedure, Integer> getNumberOfComparisons();
	
	int getTotalAssignments();
	int getOperationCount(OperationType operation);
	
	int getTotalProcedureCalls();
	
	int getCallStackDepth();
	IValue getReturnValue();
	IValue getVariableValue(IVariable id);
//	int getTotalMemory();
	
	default void printResult() {
		System.out.println(getReturnValue());
	}
}
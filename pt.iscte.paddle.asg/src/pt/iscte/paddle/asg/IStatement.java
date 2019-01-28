package pt.iscte.paddle.asg;

import java.util.List;

/**
 * Immutable
 */
public interface IStatement extends IInstruction {
	
	
	IBlock getParent();

//	default IProcedure getProcedure() {
//		IBlock b = getParent();
//		while(b != null && !(b instanceof IProcedure))
//			b = (IBlock) b.getParent();
//		
//		return (IProcedure) b;
//	}

//	default int getDepth() {
//		int d = 1;
//		IBlock b = getParent();
//		while(b != null && !(b instanceof IProcedure)) {
//			b = b.getParent();
//			d++;
//		}
//		return d;
//	}
	
	List<IExpression> getExpressionParts();
}

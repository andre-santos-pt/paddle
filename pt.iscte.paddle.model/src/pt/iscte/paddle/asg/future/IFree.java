package pt.iscte.paddle.asg.future;

import pt.iscte.paddle.model.IStatement;
import pt.iscte.paddle.model.IVariableDeclaration;

public interface IFree extends IStatement {
	IVariableDeclaration getVariable();
}

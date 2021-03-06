package pt.iscte.paddle.model;

import java.util.ArrayList;
import java.util.List;

public interface IArrayElementAssignment extends IStatement {
	List<IExpression> getIndexes(); // not null, length > 0 && length <= getDimensions
	default int getDimensions() {
		return getIndexes().size();
	}
	
	IExpression getTarget();
	IExpression getExpression();
	IBlock getParent();
	
	@Override
	default List<IExpression> getExpressionParts() {
		List<IExpression> list = new ArrayList<IExpression>(getIndexes());
		list.add(getExpression());
		return list;
	}
}

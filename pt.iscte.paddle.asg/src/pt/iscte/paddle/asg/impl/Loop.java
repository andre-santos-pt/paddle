package pt.iscte.paddle.asg.impl;

import pt.iscte.paddle.asg.IExpression;
import pt.iscte.paddle.asg.ILoop;

class Loop extends Block implements ILoop {
	private final IExpression guard;

	public Loop(Block parent, IExpression guard) {
		super(parent, true);
		this.guard = guard;
	}
	
	@Override
	public Block getParent() {
		return (Block) super.getParent();
	}
	
	@Override
	public IExpression getGuard() {
		return guard;
	}

	@Override
	public String toString() {
		return "while " + guard + " " + super.toString();
	}
}

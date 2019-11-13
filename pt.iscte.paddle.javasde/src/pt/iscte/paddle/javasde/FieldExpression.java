package pt.iscte.paddle.javasde;


public class FieldExpression extends EditorWidget {

	private EditorWidget id;
	private ExpressionWidget expression;

	public FieldExpression(EditorWidget parent, String varId) {
		super(parent, parent.mode);
		id = createId(this, varId);
		new Token(this, ".");
		expression = new ExpressionWidget(this);		
	}

	@Override
	public boolean setFocus() {
		id.setFocus();
		return true;
	}
	

	public void focusExpression() {
		id.setForeground(FONT_COLOR);
		expression.setFocus();
	}
	
}
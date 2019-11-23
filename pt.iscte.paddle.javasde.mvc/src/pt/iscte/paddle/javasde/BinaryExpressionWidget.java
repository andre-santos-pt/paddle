package pt.iscte.paddle.javasde;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class BinaryExpressionWidget extends EditorWidget {
	private ExpressionWidget left;
	private ExpressionWidget right;
	private Token op;
	private boolean brackets;
	
	private RowData data = new RowData(0,0);

	public BinaryExpressionWidget(EditorWidget parent, String operator) {
		super(parent);
		setLayout(Constants.ROW_LAYOUT_H_ZERO);
		Token lBracket = new Token(this, "(");
		lBracket.setVisible(false);
		data.exclude = true;
		left = new ExpressionWidget(this);
//		left.setLayoutData(data);
		op = new Token(this, operator, Constants.BINARY_OPERATORS_SUPPLIER);
		right = new ExpressionWidget(this);
		Token rBracket = new Token(this, ")");
//		right.setLayoutData(data);
		rBracket.setVisible(false);
		
		Menu menu = op.getMenu();
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem brack = new MenuItem(menu, SWT.NONE);
		brack.setText("( ... )");
		brack.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				brackets = !brackets;
//				lBracket.setVisible(brackets);
//				rBracket.setVisible(brackets);
				data.exclude = !brackets;
				BinaryExpressionWidget.this.requestLayout();
			}
		});
		
		MenuItem simple = new MenuItem(menu, SWT.NONE);
		simple.setText("simple expression");
		simple.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ExpressionWidget parent = (ExpressionWidget) getParent();
				parent.expression = new SimpleExpression(parent, left.toString());
				dispose();
				parent.expression.requestLayout();
			}
		});
	}
	
	@Override
	public boolean setFocus() {
		left.setFocus();
		return true;
	}

	void setLeft(String expression) {
		left.set(expression);
	}
	
	public void focusRight() {
		left.setForeground(Constants.FONT_COLOR);
		right.setFocus();
		requestLayout();
	}

	@Override
	public void toCode(StringBuffer buffer) {
		if(brackets) buffer.append("(");
		left.toCode(buffer);
		buffer.append(" " + op + " ");
		right.toCode(buffer);
		if(brackets) buffer.append(")");
	}


}
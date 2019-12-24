package pt.iscte.paddle.javasde;

import org.eclipse.swt.widgets.Text;

public enum Keyword {

	CLASS,
	STATIC,
	FINAL,
	
	BOOLEAN,
	BYTE,
	SHORT,
	INT,
	LONG,
	FLOAT,
	DOUBLE,
	CHAR,
	VOID,
	
	TRUE,
	FALSE,
	NULL,
	
	IF,
	ELSE,
	
	WHILE,
	FOR,
	RETURN,
	BREAK,
	CONTINUE,
	
	NEW;

	private static String regex = String.join("|", keywords());
	
	static final int LONGEST = CONTINUE.name().length();
			
	private static String[] keywords() {
		Keyword[] keywords = values();
		String[] v = new String[keywords.length];
		for(int i = 0; i < v.length; i++)
			v[i] = keywords[i].name().toLowerCase();
		return v;
	}

	
	static boolean is(String str) {
		return str.matches(regex);
	}
	
	@Override
	public String toString() {
		return name().toLowerCase();
	}

	public char getAccelerator() {
		return toString().charAt(0);
	}


	public boolean match(String text) {
		return name().toLowerCase().equals(text);
	}
		
	public boolean match(Text label) {
		return match(label.getText());
	}
}

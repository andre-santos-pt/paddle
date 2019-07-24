/*
 * generated by Xtext 2.17.0
 */
package pt.iscte.paddle.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import pt.iscte.paddle.parser.antlr.internal.InternalJavaliParser;
import pt.iscte.paddle.services.JavaliGrammarAccess;

public class JavaliParser extends AbstractAntlrParser {

	@Inject
	private JavaliGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_SL_COMMENT", "RULE_ML_COMMENT");
	}
	

	@Override
	protected InternalJavaliParser createParser(XtextTokenStream stream) {
		return new InternalJavaliParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "Module";
	}

	public JavaliGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(JavaliGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}

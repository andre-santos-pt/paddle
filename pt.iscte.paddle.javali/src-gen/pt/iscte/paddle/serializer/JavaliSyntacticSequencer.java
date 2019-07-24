/*
 * generated by Xtext 2.17.0
 */
package pt.iscte.paddle.serializer;

import com.google.inject.Inject;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AlternativeAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;
import pt.iscte.paddle.services.JavaliGrammarAccess;

@SuppressWarnings("all")
public class JavaliSyntacticSequencer extends AbstractSyntacticSequencer {

	protected JavaliGrammarAccess grammarAccess;
	protected AbstractElementAlias match_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__a;
	protected AbstractElementAlias match_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__p;
	protected AbstractElementAlias match_Primary___ExclamationMarkKeyword_6_0_or_LeftParenthesisKeyword_7_0__a;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (JavaliGrammarAccess) access;
		match_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__a = new GroupAlias(true, true, new TokenAlias(true, true, grammarAccess.getPrimaryAccess().getExclamationMarkKeyword_6_0()), new TokenAlias(false, false, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_7_0()));
		match_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__p = new GroupAlias(true, false, new TokenAlias(true, true, grammarAccess.getPrimaryAccess().getExclamationMarkKeyword_6_0()), new TokenAlias(false, false, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_7_0()));
		match_Primary___ExclamationMarkKeyword_6_0_or_LeftParenthesisKeyword_7_0__a = new AlternativeAlias(true, true, new TokenAlias(false, false, grammarAccess.getPrimaryAccess().getExclamationMarkKeyword_6_0()), new TokenAlias(false, false, grammarAccess.getPrimaryAccess().getLeftParenthesisKeyword_7_0()));
	}
	
	@Override
	protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		return "";
	}
	
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (transition.getAmbiguousSyntaxes().isEmpty()) return;
		List<INode> transitionNodes = collectNodes(fromNode, toNode);
		for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
			List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
			if (match_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__a.equals(syntax))
				emit_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__a(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__p.equals(syntax))
				emit_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__p(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_Primary___ExclamationMarkKeyword_6_0_or_LeftParenthesisKeyword_7_0__a.equals(syntax))
				emit_Primary___ExclamationMarkKeyword_6_0_or_LeftParenthesisKeyword_7_0__a(semanticObject, getLastNavigableState(), syntaxNodes);
			else acceptNodes(getLastNavigableState(), syntaxNodes);
		}
	}

	/**
	 * Ambiguous syntax:
	 *     ('!'* '(')*
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) {Addition.left=}
	 *     (rule start) (ambiguity) {And.left=}
	 *     (rule start) (ambiguity) {Equality.left=}
	 *     (rule start) (ambiguity) {Multiplication.left=}
	 *     (rule start) (ambiguity) {Or.left=}
	 *     (rule start) (ambiguity) {Relation.left=}
	 *     (rule start) (ambiguity) {Xor.left=}
	 */
	protected void emit_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__a(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('!'* '(')+
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) {Addition.left=}
	 *     (rule start) (ambiguity) {And.left=}
	 *     (rule start) (ambiguity) {Equality.left=}
	 *     (rule start) (ambiguity) {Multiplication.left=}
	 *     (rule start) (ambiguity) {Or.left=}
	 *     (rule start) (ambiguity) {Relation.left=}
	 *     (rule start) (ambiguity) {Xor.left=}
	 */
	protected void emit_Primary___ExclamationMarkKeyword_6_0_a_LeftParenthesisKeyword_7_0__p(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     ('!' | '(')*
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) 'new' type=Identifier
	 *     (rule start) (ambiguity) 'null' (rule start)
	 *     (rule start) (ambiguity) id=Identifier
	 *     (rule start) (ambiguity) parts+=Identifier
	 *     (rule start) (ambiguity) value=PRIMITIVE_VALUE
	 */
	protected void emit_Primary___ExclamationMarkKeyword_6_0_or_LeftParenthesisKeyword_7_0__a(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
}

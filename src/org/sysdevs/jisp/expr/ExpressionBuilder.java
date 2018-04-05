package org.sysdevs.jisp.expr;

import java.util.LinkedList;
import java.util.List;

import org.sysdevs.jisp.parser.ConstantLiteral;
import org.sysdevs.jisp.parser.Symbol;
import org.sysdevs.jisp.token.Token;

public final class ExpressionBuilder {

	private final List<Expression> expressions = new LinkedList<>();

	public void feed(Token token) {
		switch (token.type()) {
		case NUMBER:
		case STRING:
			expressions.add(new ConstantLiteral(token));
			break;
		case SYMBOL:
			expressions.add(new Symbol(token));
			break;
		case OPEN_PAREN:
		case CLOSED_PAREN:
		default:
			break;
		}
	}

	public void feed(Expression expression) {
		expressions.add(expression);
	}

	public Expression build() {
		return new SExpression(expressions);
	}

}

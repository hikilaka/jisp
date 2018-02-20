package org.sysdevs.lisp.expr;

@SuppressWarnings("serial")
public final class ExpressionParseException extends Exception {
	private final Expression expression;

	public ExpressionParseException(String message, Expression expression) {
		super(message);
		this.expression = expression;
	}
	
	public Expression expression() {
		return expression;
	}
}

package org.sysdevs.lisp.expr;

import java.util.LinkedList;
import java.util.List;

import org.sysdevs.lisp.env.Symbol;
import org.sysdevs.lisp.token.Token;

public final class ExpressionBuilder {	
	private final List<Expression> expressions = new LinkedList<>();
	
  	public void feed(Expression expression) {
		expressions.add(expression);
	}

	@SuppressWarnings("incomplete-switch")
	public void feed(Token token) {
		switch (token.type()) {
		case SYMBOL:
			expressions.add(new Value(ValueType.SYMBOL, Symbol.make(token.lexeme()), token));
			break;
		case NUMBER:
			expressions.add(new Value(ValueType.NUMBER, Integer.parseInt(token.lexeme()), token));
			break;
		case STRING:
			expressions.add(new Value(ValueType.STRING, token.lexeme(), token));
			break;
		}
	}
    
    public int size() {
        return expressions.size();
    }

	public Expression build() throws Exception {
		if (expressions.isEmpty()) {
			throw new Exception("found empty expression");
		}
		
		Expression expression = expressions.remove(0);
		
		if (!(expression instanceof Value) && !(expression instanceof FunctionExpression)) {
			throw new ExpressionParseException("first element of expression must be a symbol but found " + expression.getClass().getSimpleName(), expression);
		}

        Symbol symbol;
        
        if (expression instanceof Value) {
            Value value = Value.class.cast(expression);
            
            if (value.type() != ValueType.SYMBOL) {
                throw new ExpressionParseException(
                        "first element of expression must be a symbol", value);
            }
            symbol = value.symbol();
        } else if (expression instanceof FunctionExpression) {
            expressions.add(0, expression);
            MultiExpression multi = new MultiExpression(expressions);

            return multi;
        } else {
            throw new ExpressionParseException(
                    "first element of expression must be a symbol", expression);
        }

		return new FunctionExpression(symbol, expressions);
	}
}

package org.sysdevs.lisp.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.sysdevs.lisp.expr.Expression;
import org.sysdevs.lisp.expr.ExpressionBuilder;
import org.sysdevs.lisp.expr.MultiExpression;

public final class TokenParser {
	private final Tokenizer tokenizer;
	
	public TokenParser(Tokenizer tokenizer) {
		this.tokenizer = tokenizer;
	}
    
    public List<Expression> parse() throws Exception {
        List<Expression> expressions = new ArrayList<>();
        Optional<Expression> expr = parseExpr();

        while (expr.isPresent()) {
            Expression expression = expr.get();
            
            if (expression instanceof MultiExpression) {
                expression.args().forEach(expressions::add);
            } else {
                expressions.add(expression);
            }
            expr = parseExpr();
        }
        
        return expressions;
    }

	private Optional<Expression> parseExpr() throws Exception {
		Optional<Token> token = tokenizer.next();
		ExpressionBuilder builder = new ExpressionBuilder();

		while (token.isPresent()) {
			switch (token.get().type()) {
			case OPEN_PAREN:
                Optional<Expression> exp = parseExpr();
                if (!exp.isPresent()) {
                    return Optional.of(builder.build());
                }
				builder.feed(exp.get());
                break;
			case CLOSED_PAREN:
                return Optional.of(builder.build());
			case NUMBER:
			case STRING:
			case SYMBOL:
				builder.feed(token.get());
				break;
			}
			token = tokenizer.next();
		}
        
        if (builder.size() == 0) {
            return Optional.empty();
        }

		return Optional.of(builder.build());
	}
}

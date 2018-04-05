package org.sysdevs.jisp.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.expr.ExpressionBuilder;
import org.sysdevs.jisp.expr.SExpression;
import org.sysdevs.jisp.token.Token;

public final class Parser {

	private final Stream<Token> tokenStream;

	public Parser(Stream<Token> tokenStream) {
		this.tokenStream = tokenStream;
	}

	public List<Expression> parse() {
		List<Expression> expressions = new ArrayList<>();
		Iterator<Token> iterator = tokenStream.sequential().iterator();

		while (iterator.hasNext()) {
			expressions.add(parseExpr(iterator));
		}

		return expressions;
	}

	private Expression parseExpr(Iterator<Token> iterator) {
		ExpressionBuilder builder = new ExpressionBuilder();

		while (iterator.hasNext()) {
			Token token = iterator.next();

			switch (token.type()) {
			case OPEN_PAREN:
				Expression expr = parseExpr(iterator);

				builder.feed(expr);
				break;
			case CLOSED_PAREN:
				return builder.build();
			case NUMBER:
			case STRING:
			case SYMBOL:
				builder.feed(token);
				break;
			}
		}

		Expression expr = builder.build();

		if (expr instanceof SExpression) {
			SExpression sexp = SExpression.class.cast(expr);
			if (sexp.getArguments().size() == 1) {
				return expr.getArguments().get(0);
			}
		}

		return expr;
	}

}

package org.sysdevs.jisp.expr;

import java.util.Collections;
import java.util.List;

import org.sysdevs.jisp.env.Environment;

public class SExpression implements Expression {
	private final List<Expression> expressions;

	public SExpression(List<Expression> expressions) {
		this.expressions = expressions;
	}

	@Override
	public Expression evaluate(Environment env) throws Exception {
		if (expressions.isEmpty()) {
			return new SExpression(Collections.emptyList());
		}

		Expression result = expressions.remove(0).evaluate(env);

		if (result instanceof JavaFunction) {
			JavaFunction function = JavaFunction.class.cast(result);
			return function.evaluate(env, expressions);
		} else if (result instanceof Lambda) {
			Lambda lambda = Lambda.class.cast(result);
			return lambda.evaluate(env, expressions);
		} else {
			return result.evaluate(env);
		}
	}

	@Override
	public List<Expression> getArguments() {
		return expressions;
	}

	@Override
	public String toString() {
		return "sexpr" + expressions;
	}
}

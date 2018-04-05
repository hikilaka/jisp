package org.sysdevs.jisp.expr;

import java.util.Collections;
import java.util.List;

import org.sysdevs.jisp.env.Environment;
import org.sysdevs.jisp.parser.Symbol;

public final class BindingExpression implements Expression {
	private final Expression bindings;

	private final Expression body;

	public BindingExpression(Expression bindings, Expression body) {
		this.bindings = bindings;
		this.body = body;
	}

	@Override
	public Expression evaluate(Environment parent) throws Exception {
		Environment env = new Environment(parent);

		// TODO: ensure parameters only contains K/V pairs
		for (int i = 0; i < bindings.getArguments().size(); i += 2) {
			Expression identifier = bindings.getArguments().get(i);
			Expression value = bindings.getArguments().get(i + 1).evaluate(env).evaluate(env);

			// TODO: ensure that identifier is indeed a symbol
			env.define(Symbol.class.cast(identifier), value);
		}

		Expression result = body.evaluate(env);
		
		while (!body.getArguments().isEmpty()) {
			result = body.evaluate(env);
		}
		
		return result;
	}

	@Override
	public List<Expression> getArguments() {
		return Collections.emptyList();
	}
}

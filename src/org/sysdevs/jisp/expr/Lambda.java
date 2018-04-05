package org.sysdevs.jisp.expr;

import java.util.List;

import org.sysdevs.jisp.env.Environment;
import org.sysdevs.jisp.env.StandardEnvironment;
import org.sysdevs.jisp.parser.Symbol;

public final class Lambda implements Expression {
	private final Expression parameters;

	private final Expression body;

	public Lambda(Expression parameters, Expression body) {
		this.parameters = parameters;
		this.body = body;
	}

	@Override
	public Expression evaluate(Environment parent) throws Exception {
		Environment env = new Environment(parent);

		// TODO: ensure parameters only contains K/V pairs
		for (int i = 0; i < parameters.getArguments().size(); i++) {
			Expression identifier = parameters.getArguments().get(i).evaluate(env);
			Expression value = parameters.getArguments().get(i + 1).evaluate(env);

			// TODO: ensure that identifier is indeed a symbol
			env.define(Symbol.class.cast(identifier), value);
		}

		Expression result = body.evaluate(env);

		while (!body.getArguments().isEmpty()) {
			result = body.evaluate(env);
		}
		return result;
	}

	public Expression evaluate(Environment parent, List<Expression> args) {
		try {
			Environment env = new Environment(parent);

			for (int i = 0; i < parameters.getArguments().size(); i++) {
				Expression identifier = parameters instanceof SExpression ? parameters.getArguments().get(i)
						: parameters;

				env.define(Symbol.class.cast(identifier), args.get(i).evaluate(env));
			}

			return body.evaluate(env);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return StandardEnvironment.NIL;
	}

	@Override
	public List<Expression> getArguments() {
		return parameters.getArguments();
	}
}

package org.sysdevs.jisp.expr;

import java.util.Collections;
import java.util.List;

import org.sysdevs.jisp.env.Environment;

public final class JavaFunction implements Expression {
	private final JavaFunctionBody body;

	public JavaFunction(JavaFunctionBody body) {
		this.body = body;
	}

	public Expression evaluate(Environment env, List<Expression> args) {
		return body.apply(env, args);
	}

	@Override
	public Expression evaluate(Environment env) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Expression> getArguments() {
		return Collections.emptyList();
	}
}

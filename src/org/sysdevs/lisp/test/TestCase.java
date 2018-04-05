package org.sysdevs.lisp.test;

import org.sysdevs.jisp.env.Environment;
import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.expr.Lambda;

public final class TestCase {
	private final String description;
	
	private final Expression expectedValue;
	
	private final Lambda function;
	
	public TestCase(String description, Expression expectedValue, Lambda function) {
		this.description = description;
		this.expectedValue = expectedValue;
		this.function = function;
	}
	
	public boolean passes(Environment env) throws Exception {
		return function.evaluate(env).equals(expectedValue);
	}
	
	public String description() {
		return description;
	}
	
	public Expression expectedValue() {
		return expectedValue;
	}
	
	public Lambda function() {
		return function;
	}
}


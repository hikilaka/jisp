package org.sysdevs.jisp.expr;

import java.util.List;

import org.sysdevs.jisp.env.Environment;

public interface Expression {

	public Expression evaluate(Environment env) throws Exception;

	public List<Expression> getArguments();

}

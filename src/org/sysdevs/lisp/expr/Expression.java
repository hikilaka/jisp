package org.sysdevs.lisp.expr;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.sysdevs.lisp.env.Environment;
import org.sysdevs.lisp.token.Token;

public class Expression {
	public static final List<Expression> NO_ARGS = Collections.emptyList();
	
	private final Token token;
    private final List<Expression> args = new LinkedList<>();
    
    public Expression(Token token) {
    	this(token, NO_ARGS);
    }
    
    public Expression(List<Expression> args) {
    	this(null, args);
    }
    
    public Expression(Token token, List<Expression> args) {
    	this.token = token;
    	this.args.addAll(args);
    }

    public Value evaluate(Environment env, List<Expression> args) {
        throw new UnsupportedOperationException();
    }

    public Value evaluate(Environment env) {
    	return evaluate(env, args);
    }

    public Token token() {
    	return token;
    }

    public List<Expression> args() {
    	return args;
    }

    @Override
    public String toString() {
    	return "Expression{["
    			+ args.stream()
    				  .map(e -> e.toString())
    				  .collect(Collectors.joining(", ")) + "]}";
    }
}

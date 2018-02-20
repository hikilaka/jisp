package org.sysdevs.lisp.expr;	

import java.util.List;

import org.sysdevs.lisp.env.Environment;
import org.sysdevs.lisp.env.Symbol;

public class FunctionExpression extends Expression {
	private final Symbol symbol;
	
	public FunctionExpression(Symbol symbol, List<Expression> args) {
		super(args);
		this.symbol = symbol;
	}

    @Override
	public Value evaluate(Environment env, List<Expression> args) {
        Expression function = env.get(symbol);
        
        if (function == null) {
            throw new RuntimeException("cannot find symbol '" + symbol.name() + "'");
        }

        return function.evaluate(env, args);
    }

	public Symbol symbol() {
		return symbol;
	}
    
    @Override
    public String toString() {
    	return "function<" + symbol + ">";
    }
}

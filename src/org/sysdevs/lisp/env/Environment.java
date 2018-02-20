package org.sysdevs.lisp.env;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.sysdevs.lisp.expr.Expression;

public final class Environment {    
    private final Map<Symbol, Expression> mapping = new HashMap<>();
    
    public void set(Symbol symbol, Expression expr) {
        mapping.put(symbol, expr);
    }
    
    public void set(LispFunction function) {
        set(function.name(), function);
    }

    public Expression get(Symbol symbol) {
    	Expression expr = mapping.get(symbol);
    	
    	if (expr == null) {
    		return Symbol.NIL_VALUE;
    	}

        return expr;
    }
    
    @Override
    public String toString() {
        return "Environment{" + mapping.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining(", "))
            + "}";
    }
}

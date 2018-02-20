package org.sysdevs.lisp.env;

import java.util.List;
import java.util.function.BiFunction;

import org.sysdevs.lisp.expr.Expression;
import org.sysdevs.lisp.expr.Value;

public class LispFunction extends Expression {
    private final Symbol name;
    
    private final BiFunction<Environment, List<Expression>, Expression> body;
    
    public LispFunction(String name, BiFunction<Environment, List<Expression>, Expression> body) {
        this(Symbol.make(name), body);
    }
    
    public LispFunction(Symbol name, BiFunction<Environment, List<Expression>, Expression> body) {
        super(NO_ARGS);
        this.name = name;
        this.body = body;
    }
    
    @Override
    public Value evaluate(Environment env, List<Expression> args) {
        return body.apply(env, args).evaluate(env);
    }

    public Symbol name() {
        return name;
    }
}

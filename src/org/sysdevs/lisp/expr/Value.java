package org.sysdevs.lisp.expr;

import java.util.Arrays;
import java.util.List;
import org.sysdevs.lisp.env.Environment;
import org.sysdevs.lisp.env.Symbol;
import org.sysdevs.lisp.token.Token;

public final class Value extends Expression {
	private final ValueType type;
	private final Object value;
	
	public Value(ValueType type, Object value) {
        this(type, value, null);
	}
    
    public Value(ValueType type, Object value, Token token) {
		super(token);
		this.type = type;
		this.value = value;
	}

	@Override
	public Value evaluate(Environment env, List<Expression> args) {
        if (type == ValueType.SYMBOL) {
            Expression expr = env.get(symbol());
            
            Value[] builtIn = new Value[] { Symbol.NIL_VALUE, Symbol.FALSE_VALUE, Symbol.TRUE_VALUE };

            if (Arrays.stream(builtIn).noneMatch(v -> v == expr)) {
                return expr.evaluate(env);
            }
        }
		return this;
	}

	public ValueType type() {
		return type;
	}
    
    public Object object() {
        return value;
    }
	
	public Symbol symbol() {
		return Symbol.class.cast(value);
	}
	
	public String string() {
		return String.class.cast(value);
	}
	
	public Integer integer() {
		return Integer.class.cast(value);
	}
	
    @Override
    public String toString() {
    	return value.toString();
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Value)) {
            return false;
        }

        Value v = Value.class.cast(other);
        return v.value.equals(value);
    }
}

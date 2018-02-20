
package org.sysdevs.lisp.env;

import java.util.List;
import java.util.stream.Collectors;
import org.sysdevs.lisp.expr.Expression;
import org.sysdevs.lisp.expr.Value;
import org.sysdevs.lisp.expr.ValueType;

public class GlobalEnvironment {
	public static final LispFunction PRINT_LINE = new LispFunction("print-line", (Environment env, List<Expression> args) -> {
		System.out.println(args.stream()
				.map(e -> e.evaluate(env).toString())
				.collect(Collectors.joining()));
		return Symbol.NIL_VALUE;
    });
	
	public static final LispFunction IS_NIL = new LispFunction("nil?", (Environment env, List<Expression> args) -> {
		return args.stream()
				.map(e -> e.evaluate(env))
				.anyMatch(e -> e == Symbol.NIL_VALUE)
				? Symbol.TRUE_VALUE : Symbol.FALSE_VALUE;
    });

    public static final LispFunction DEFINE = new LispFunction("define", (Environment env, List<Expression> args) -> {
        if (args.size() < 2) {
            System.out.println("expected function 'define' to have two arguments");
            return Symbol.NIL_VALUE;
        }

        Value key = args.get(0).evaluate(env);
        
        if (key.type() != ValueType.SYMBOL) {
            System.out.println("expected first argument to evaluate to symbol");
            return Symbol.NIL_VALUE;
        }
        
        Value value = args.get(1).evaluate(env);

        env.set(key.symbol(), value);
        return key;
    });
    
    public static final LispFunction IF = new LispFunction("if", (Environment env, List<Expression> args) -> {
        if (args.size() < 2) {
            System.out.println("expected function 'if' to have at least a predicate and body");
            return Symbol.NIL_VALUE;
        }
        
        Expression predicate = args.get(0).evaluate(env);
       
        if (predicate.equals(Symbol.TRUE_VALUE)) {
            return args.get(1).evaluate(env);
        } else if (args.size() > 2) {
            return args.get(2).evaluate(env);
        }
        return Symbol.NIL_VALUE;
    });
    
    public static final LispFunction EQUALS = new LispFunction("=", (Environment env, List<Expression> args) -> {
        // NOT EFFICIENT AT ALL!
        for (int i = 1; i < args.size(); i++) {
            Value prev = args.get(i - 1).evaluate(env);
            Value curr = args.get(i).evaluate(env);
            
            if (!curr.equals(prev)) {
                return Symbol.FALSE_VALUE;
            }
        }

        return Symbol.TRUE_VALUE;
    });
    
    public static final LispFunction ADD = new LispFunction("+", (Environment env, List<Expression> args) -> {
        if (args.isEmpty()) {
            System.out.println("expected function '+' at least one argument");
            return Symbol.NIL_VALUE;
        }

        // evaluate the arguments
        List<Value> values = args.stream()
            .map(e -> e.evaluate(env))
            .collect(Collectors.toList());

        boolean allNumbers = values.stream().allMatch(v -> v.type() == ValueType.NUMBER);
        boolean allStrings = values.stream().allMatch(v -> v.type() == ValueType.STRING);
        ValueType type = allNumbers ? ValueType.NUMBER : (allStrings ? ValueType.STRING : ValueType.SYMBOL);

        switch (type) {
            case NUMBER:
                return new Value(type, values.stream()
                        .mapToInt(v -> v.integer())
                        .sum());
            case STRING:
                return new Value(type, values.stream()
                		.map(Object::toString)
                		.collect(Collectors.joining()));
            case SYMBOL: {
                return new Value(type, Symbol.make(values.stream()
                		.map(Object::toString)
                		.collect(Collectors.joining())));
            }
        }
        
        // this shouldn't happen
        return null;
    });
    
    public static final LispFunction MULTIPLY = new LispFunction("*", (Environment env, List<Expression> args) -> {
        if (args.isEmpty()) {
            System.out.println("expected function '*' at least one argument");
            return Symbol.NIL_VALUE;
        }

        // TODO: this function only accepts number values,
        // ensure that all args are numbers!
        
        // evaluate the arguments
        List<Value> values = args.stream()
            .map(e -> e.evaluate(env))
            .collect(Collectors.toList());

        boolean allNumbers = values.stream().allMatch(v -> v.type() == ValueType.NUMBER);
        boolean allStrings = values.stream().allMatch(v -> v.type() == ValueType.STRING);
        ValueType type = allNumbers ? ValueType.NUMBER : (allStrings ? ValueType.STRING : ValueType.SYMBOL);

        switch (type) {
            case NUMBER:
                return new Value(type, values.stream()
                        .mapToInt(v -> v.integer())
                        .sum());
            case STRING:
                return new Value(type, values.stream()
                		.map(Object::toString)
                		.collect(Collectors.joining()));
            case SYMBOL: {
                return new Value(type, Symbol.make(values.stream()
                		.map(Object::toString)
                		.collect(Collectors.joining())));
            }
        }
        
        // this shouldn't happen
        return null;
    });
    
    public static Environment build() {
        Environment env = new Environment();
        env.set(Symbol.NIL, Symbol.NIL_VALUE);
        env.set(Symbol.TRUE, Symbol.TRUE_VALUE);
        env.set(Symbol.FALSE, Symbol.FALSE_VALUE);

        env.set(PRINT_LINE);
        env.set(IS_NIL);
        env.set(DEFINE);
        env.set(IF);
        env.set(EQUALS);
        env.set(ADD);
        return env;
    }
}

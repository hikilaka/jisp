package org.sysdevs.lisp.env;

import java.util.Objects;

import org.sysdevs.lisp.expr.Value;
import org.sysdevs.lisp.expr.ValueType;

public final class Symbol {
	public static Symbol NIL = Symbol.make("nil");
	public static Value NIL_VALUE = new Value(ValueType.SYMBOL, NIL);

	public static Symbol TRUE = Symbol.make("true");
	public static Value TRUE_VALUE = new Value(ValueType.SYMBOL, TRUE);

	public static Symbol FALSE = Symbol.make("false");
	public static Value FALSE_VALUE = new Value(ValueType.SYMBOL, FALSE);

	public static Symbol make(String name) {
		return new Symbol(name);
	}
	
	private final String name;
	
	public Symbol(String name) {
		this.name = name;
	}
	
	public String name() {
		return name;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof Symbol)) {
			return false;
		}
		
		Symbol other = Symbol.class.cast(object);
		return other.name.equals(name);
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }
	
	@Override
	public String toString() {
		return name;
	}
}

package org.sysdevs.jisp.env;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.parser.Symbol;

public class Environment {
	private final Optional<Environment> parent;

	private final Map<String, Expression> mapping = new HashMap<>();

	public Environment() {
		this(null);
	}

	public Environment(Environment parent) {
		this.parent = Optional.ofNullable(parent);
	}

	public void define(String key, Expression value) {
		mapping.put(key, value);
	}

	public void define(Symbol key, Expression value) {
		mapping.put(key.getToken().lexeme(), value);
	}

	public Optional<Expression> get(String key) {
		Optional<Expression> result = Optional.ofNullable(mapping.get(key));

		if (!result.isPresent() && parent.isPresent()) {
			return parent.get().get(key);
		} else {
			return result;
		}
	}

	public Optional<Expression> get(Symbol key) {
		return get(key.getToken().lexeme());
	}

	public boolean has(String key) {
		return mapping.containsKey(key);
	}

	public boolean has(Symbol key) {
		return has(key.getToken().lexeme());
	}

	@Override
	public String toString() {
		return "env {\n" + (parent.isPresent() ? "parent=[" + parent.get() + "]" : "")
				+ mapping.keySet().stream().map(k -> {
					return String.format("\t%s -> %s", k, get(k).orElse(null));
				}).collect(Collectors.joining(",\n")) + "\n}";
	}
}

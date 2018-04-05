package org.sysdevs.jisp.parser;

import java.util.Collections;
import java.util.List;

import org.sysdevs.jisp.env.Environment;
import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.token.Token;
import org.sysdevs.jisp.token.TokenType;

public final class Symbol implements Expression {
	private final Token token;

	public Symbol(Token token) {
		this.token = token;
	}

	public Symbol(String lexeme) {
		this.token = new Token(TokenType.STRING, lexeme);
	}

	public Token getToken() {
		return token;
	}

	@Override
	public Expression evaluate(Environment env) {
		return env.get(this).orElse(this);
	}

	@Override
	public List<Expression> getArguments() {
		return Collections.emptyList();
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
		return other.token.lexeme().equals(token.lexeme());
	}

	@Override
	public String toString() {
		return token.lexeme();
	}
}

package org.sysdevs.jisp.parser;

import java.util.Collections;
import java.util.List;

import org.sysdevs.jisp.env.Environment;
import org.sysdevs.jisp.expr.Expression;
import org.sysdevs.jisp.token.Token;
import org.sysdevs.jisp.token.TokenType;

public final class ConstantLiteral implements Expression {
	private final Token token;

	public ConstantLiteral(String value) {
		this(new Token(TokenType.STRING, value));
	}

	public ConstantLiteral(int value) {
		this(new Token(TokenType.NUMBER, String.valueOf(value)));
	}

	public ConstantLiteral(Token token) {
		this.token = token;
	}

	public Token getToken() {
		return token;
	}

	public Integer asNumber() {
		return Integer.parseInt(token.lexeme());
	}

	public String asString() {
		return token.lexeme();
	}

	public boolean isNumber() {
		return token.type() == TokenType.NUMBER;
	}

	public boolean isString() {
		return token.type() == TokenType.STRING;
	}

	@Override
	public Expression evaluate(Environment env) {
		return this;
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
		if (!(object instanceof ConstantLiteral)) {
			return false;
		}

		ConstantLiteral other = ConstantLiteral.class.cast(object);
		return other.token.type() == token.type() && other.token.lexeme().equals(token.lexeme());
	}

	@Override
	public String toString() {
		return token.lexeme();
	}
}

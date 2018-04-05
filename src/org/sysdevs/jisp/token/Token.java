package org.sysdevs.jisp.token;

/**
 * Represents a piece of 'tokenized' source code. A token includes a type, its
 * row and column in the source, and, finally, its lexeme (text).
 * 
 * {@see TokenType}
 * 
 * @author Zack Penn
 */
public final class Token {

	private final TokenType type;

	private final String lexeme;

	private final int row;

	private final int column;

	public Token(TokenType type, String lexeme) {
		this(type, lexeme, 0, 0);
	}

	public Token(TokenType type, String lexeme, int row, int column) {
		this.type = type;
		this.lexeme = lexeme;
		this.row = row;
		this.column = column;
	}

	public TokenType type() {
		return type;
	}

	public String lexeme() {
		return lexeme;
	}

	public int row() {
		return row;
	}

	public int column() {
		return column;
	}

	@Override
	public String toString() {
		return "Token{" + type + ", \"" + lexeme + "\", " + column + ":" + row + "}";
	}

}

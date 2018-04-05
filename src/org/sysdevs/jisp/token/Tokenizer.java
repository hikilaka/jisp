package org.sysdevs.jisp.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.sysdevs.jisp.buffer.InputBuffer;

/**
 * A Tokenizer takes an instance of an {@link InputBuffer} and converts it into
 * a stream of {@link Token}s to be parsed.
 * 
 * @author Zack Penn
 */
public final class Tokenizer {

	private final InputBuffer buffer;

	private int row = 1;

	private int column = 1;

	public Tokenizer(InputBuffer buffer) {
		this.buffer = buffer;
	}

	public Stream<Token> stream() {
		List<Token> tokens = new ArrayList<>();
		Optional<Token> token = next();

		while (token.isPresent()) {
			tokens.add(token.get());
			token = next();
		}

		return tokens.stream();
	}

	public Optional<Token> next() {
		Optional<Character> ch = buffer.peekCharacter();

		if (!ch.isPresent()) {
			return Optional.empty();
		}

		switch (ch.get()) {
		case '(':
		case '[':
			return Optional.of(new Token(TokenType.OPEN_PAREN, buffer.take(1).get(), row++, column));

		case ')':
		case ']':
			return Optional.of(new Token(TokenType.CLOSED_PAREN, buffer.take(1).get(), row++, column));

		// skip whitespace for now (should we return a token?)
		// remember row starts at 1
		case '\n':
			row = 1;
			column++;
		case '\r':
			buffer.advance(1);
			return next();
		case '\t':
		case ' ':
			row += 1;
			buffer.advance(1);
			return next();

		// comments begin with a ;
		case ';':
			return handleComment();

		default:
			if (ch.get() >= '0' && ch.get() <= '9') {
				return parseInteger();
			} else if (ch.get() == '"') {
				return parseString();
			}
			return parseSymbol();
		}
	}

	public int row() {
		return row;
	}

	public int column() {
		return column;
	}

	private Optional<Token> handleComment() {
		Optional<Character> ch = buffer.peekCharacter();
		int offset = 0;

		while (ch.isPresent() && (ch.get() != '\n')) {
			offset += 1;
			ch = buffer.peekCharacter(offset);
		}

		row += offset;
		column += 1;
		buffer.advance(offset + 1);
		return next();
	}

	private Optional<Token> parseString() {
		buffer.advance(1);
		Optional<Character> ch = buffer.peekCharacter(0);
		int offset = 0;
		boolean escaped = false;

		while (ch.isPresent()) {
			escaped = (ch.get() == '\\' && !escaped);

			if (ch.get() == '"' && !escaped) {
				break;
			}

			offset += 1;
			ch = buffer.peekCharacter(offset);
		}

		if (offset > 0) {
			row += offset + 2;
			String value = buffer.take(offset).get();
			buffer.advance(1);
			return Optional.of(new Token(TokenType.STRING, value, row - offset - 1, column));
		} else {
			return Optional.empty();
		}
	}

	private Optional<Token> parseInteger() {
		Optional<Character> ch = buffer.peekCharacter();
		int offset = 0;

		while (ch.isPresent() && ch.get() >= '0' && ch.get() <= '9') {
			offset += 1;
			ch = buffer.peekCharacter(offset);
		}

		if (offset > 0) {
			row += offset;
			return Optional.of(new Token(TokenType.NUMBER, buffer.take(offset).get(), row - offset, column));
		} else {
			// no chars 0-9 were able to be read
			return Optional.empty();
		}
	}

	private Optional<Token> parseSymbol() {
		Optional<Character> ch = buffer.peekCharacter();
		int offset = 0;

		while (ch.isPresent() && validSymbolChar(ch.get(), offset)) {
			offset += 1;
			ch = buffer.peekCharacter(offset);
		}

		if (offset > 0) {
			row += offset;
			return Optional.of(new Token(TokenType.SYMBOL, buffer.take(offset).get(), row - offset, column));
		} else {
			return Optional.empty();
		}
	}

	private boolean validSymbolChar(Character ch, int offset) {
		if (ch >= 'a' && ch <= 'z') {
			return true;
		}
		if (ch >= 'A' && ch <= 'Z') {
			return true;
		}
		if (offset > 0 && ch >= '0' && ch <= '9') {
			// special condition, symbols cannot start with a number
			return true;
		}
		switch (ch) {
		case '!':
		case '$':
		case '%':
		case '^':
		case '&':
		case '*':
		case '-':
		case '+':
		case '=':
		case '|':
		case '/':
		case '?':
		case '>':
		case '<':
			return true;
		}
		return false;
	}

}

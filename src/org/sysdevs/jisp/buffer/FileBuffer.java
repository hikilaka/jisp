package org.sysdevs.jisp.buffer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * An implementation of an {@link InputBuffer} that accepts a {@link Path} for
 * its source of input.
 * 
 * @author Zack Penn
 */
public final class FileBuffer implements InputBuffer {

	private final String buffer;

	private int position = 0;

	public FileBuffer(Path file) throws IOException {
		buffer = new String(Files.readAllBytes(file));
	}

	@Override
	public Optional<Character> peekCharacter(int offset) {
		int index = position + offset;

		if (index >= buffer.length()) {
			return Optional.empty();
		}

		return Optional.of(buffer.charAt(index));
	}

	@Override
	public Optional<String> peekString(int length, int offset) {
		int index = position + offset;
		int endIndex = Math.min(index + length, buffer.length() - 1);

		if (index >= buffer.length()) {
			return Optional.empty();
		}

		return Optional.of(buffer.substring(index, endIndex));
	}

	@Override
	public Optional<String> take(int length) {
		advance(length);
		return peekString(length, -length);
	}

	@Override
	public Optional<String> take(int length, int offset) {
		advance(length);
		return peekString(length, offset - length);
	}

	@Override
	public void advance(int amount) {
		position += amount;
	}

}
